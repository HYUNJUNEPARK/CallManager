package com.module.callmanager.ui

import android.Manifest
import android.app.Application
import androidx.core.content.PermissionChecker
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.module.callmanager.R
import com.module.callmanager.data.CallLogLocalDataSource
import com.module.callmanager.model.log.CallLogList
import com.module.callmanager.model.log.LogType
import com.module.callmanager.util.LogUtil

class CallLogViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val callLogLocalDataSource = CallLogLocalDataSource(context)

    //콜 로그 데이터
    private var _callLogList = MutableLiveData<CallLogList>()
    val callLogList: LiveData<CallLogList>
        get() = _callLogList

    //수신 로그 데이터
    private var _incomingCallLogList = MutableLiveData<CallLogList>()
    val incomingCallLogList: LiveData<CallLogList>
        get() = _incomingCallLogList

    //발신 로그 데이터
    private var _outgoingCallLogList = MutableLiveData<CallLogList>()
    val outgoingCallLogList: LiveData<CallLogList>
        get() = _outgoingCallLogList

    //부재중 로그 데이터
    private val _missedCallLogList = MutableLiveData<CallLogList>()
    val missedCallLogList: LiveData<CallLogList>
        get() = _missedCallLogList

    /**
     * 다비이스 내 콜로그를 캐시 callLogList 에 초기화한다.
     */
    fun getAllCallLog() {
        _callLogList.value = callLogLocalDataSource.getAllCallLog() ?: return
        LogUtil.logD("callLogList(LiveData) : ${callLogList.value}")
    }

    /**
     * 콜로그 캐시 중 원하는 타입의 기록만 필터링해 캐시를 초기화한다.
     * 모듈 사용자는 CallLogViewModel 클래스 멤버 변수 incomingCallLogList / outgoingCallLogList / missedCallLogList 에서 필요한 데이터를 가져온다.
     * @param logType LogType.OUTGOING() / LogType.INCOMING() / LogType.MISSED() 기능만 구현되어 있음
     */
    fun getCallLog(logType: LogType) {
        //콜로그 접근 권한이 없는 경우
        if (PermissionChecker.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PermissionChecker.PERMISSION_DENIED) {
            LogUtil.logE(context.getString(R.string.permission_denied_call_log))
            
            return
        }

        //콜로그 접근 권한이 있는 경우
        //콜로그 관련 캐시데이터가 없다면 캐시 데이터 초기화
        if(callLogList.value == null) {
            getAllCallLog()
        }
        val callLogType = logType.type
        val sortedCallLogList = CallLogList()

        //콜 기록 캐시 데이터(callLogList)에서 원하는 타입의 콜로그만 필터링
        for(callLog in callLogList.value!!) {
            if (callLog?.type == callLogType) {
                sortedCallLogList.add(callLog)
            }
        }

        //수신 로그
        if (callLogType == LogType.INCOMING.type) {
            _incomingCallLogList.value = sortedCallLogList
            LogUtil.logD("incoming log: ${incomingCallLogList.value}")
        }

        //발신 로그
        if (callLogType == LogType.OUTGOING.type) {
            _outgoingCallLogList.value = sortedCallLogList
            LogUtil.logD("outgoing log: ${outgoingCallLogList.value}")
        }

        //부재중 로그
        if (callLogType == LogType.MISSED.type) {
            _missedCallLogList.value = sortedCallLogList
            LogUtil.logD("missed log: ${missedCallLogList.value}")
        }

        if (callLogType == LogType.VOICEMAIL.type) {

        }

        if (callLogType == LogType.REJECTED.type) {

        }

        if (callLogType == LogType.BLOCKED.type) {

        }

        if (callLogType == LogType.ANSWERED_EXTERNALLY_TYPE.type) {

        }
    }

    /**
     * 콜 로그 캐시 데이터를 삭제한다.
     * 디바이스 내 로그 데이터가 완전히 삭제되면, 콜 로그 관련 캐시도 clear
     */
    fun deleteAllCallLog() {
        //콜로그 접근 권한이 없는 경우
        if (PermissionChecker.checkSelfPermission(context, Manifest.permission.WRITE_CALL_LOG) == PermissionChecker.PERMISSION_DENIED) {
            LogUtil.logE(context.getString(R.string.permission_denied_write_call_log))
            return
        }

        //콜로그 접근 권한이 있는 경우
        callLogLocalDataSource.deleteAllCallLog().let { result ->
            if (result) {
                _callLogList.value?.clear()
                _incomingCallLogList.value?.clear()
                _outgoingCallLogList.value?.clear()
                _missedCallLogList.value?.clear()
            }
        }
    }

    /**
     * 디바이스 내 특정 콜 로그를 삭제한다.
     * 리사이클러 뷰에서 콜로그 기록을 선택적으로 삭제하는 경우 사용하려고 만든 메서드
     * 디바이스 로그 데이터가 완전히 삭제되면, getAllCallLog()가 호출되어 캐시 초기화
     * @param logIdList 지우려하는 콜로그 ID 리스트
     */
    fun deleteCallLog(logIdList: ArrayList<String>) {
        //콜로그 접근 권한이 없는 경우
        if (PermissionChecker.checkSelfPermission(context, Manifest.permission.WRITE_CALL_LOG) == PermissionChecker.PERMISSION_DENIED) {
            LogUtil.logE(context.getString(R.string.permission_denied_write_call_log))
            return
        }

        //콜로그 접근 권한이 있는 경우
        callLogLocalDataSource.deleteCallLog(logIdList).let { result -> //1. 디바이스 내 로그 데이터를 정상적으로 지웠다면,
            if (result) {
                getAllCallLog() //2. 캐시를 초기화
            }
        }
    }
}