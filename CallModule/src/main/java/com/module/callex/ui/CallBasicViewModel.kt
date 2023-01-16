package com.module.callex.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.module.callex.data.CallBasicLocalDataSource
import com.module.callex.data.CallCache
import com.module.callex.data.model.contact.ContactItem
import com.module.callex.data.model.contact.ContactList
import com.module.callex.data.model.log.CallLogItem
import com.module.callex.data.model.log.CallLogList
import com.module.callex.data.model.log.LogType

class CallBasicViewModel(application: Application) : AndroidViewModel(application) {
    private val callBasicLocalDataSource = CallBasicLocalDataSource(application)

    /**
     * 디바이스 연락처 데이터
     */
    private val _contactList = MutableLiveData<ContactList>()
    val contactList: LiveData<ContactList>
        get() = _contactList

    /**
     * 모든 콜 로그 데이터
     */
    private val _callLogList = MutableLiveData<CallLogList>()
    val callLogList: LiveData<CallLogList>
        get() = _callLogList

    /**
     * 수신 로그 데이터
     */
    private val _incomingCallLogList = MutableLiveData<CallLogList>()
    val incomingCallLogList: LiveData<CallLogList>
        get() = _incomingCallLogList

    /**
     * 발신 로그 데이터
     */
    private val _outgoingCallLogList = MutableLiveData<CallLogList>()
    val outgoingCallLogList: LiveData<CallLogList>
        get() = _outgoingCallLogList

    /**
     * 부재중 로그 데이터
     */
    private val _missedCallLogList = MutableLiveData<CallLogList>()
    val missedCallLogList: LiveData<CallLogList>
        get() = _missedCallLogList

    /**
     * 디바이스 모든 연락처를 가져온다.
     */
    fun getContacts() {
        _contactList.value = callBasicLocalDataSource.getContacts()
        println("getContacts() contactList(LiveData) : ${contactList.value}")
    }

    /**
     * 다비이스 모든 콜로그를 가져온다.
     */
    fun getAllCallLog() {
        _callLogList.value = callBasicLocalDataSource.getAllCallLog()
        println("getAllCallLog() callLogList(LiveData) : ${callLogList.value}")
    }

    /**
     * 콜로그 중 원하는 타입의 기록만 가져온다.
     */
    fun getCallLog(logType: LogType) {
        if(callLogList.value == null) {
            getAllCallLog()
        }

        val type = logType.type
        val sortedCallLogList = CallLogList()

        for(callLog in callLogList.value!!) {
            if (callLog?.type == type) {
                println("callLogType: $callLog")
                sortedCallLogList.add(callLog)
            }
        }

        if (type == LogType.INCOMING.type) {
            _incomingCallLogList.value = sortedCallLogList
            println("incoming: ${incomingCallLogList.value}")
        }

        if (type == LogType.OUTGOING.type) {
            _outgoingCallLogList.value = sortedCallLogList
            println("outgoing: ${outgoingCallLogList.value}")
        }

        if (type == LogType.MISSED.type) {
            _missedCallLogList.value = sortedCallLogList
            println("missed: ${missedCallLogList.value}")
        }

        if (type == LogType.VOICEMAIL.type) {

        }

        if (type == LogType.REJECTED.type) {

        }

        if (type == LogType.BLOCKED.type) {

        }

        if (type == LogType.ANSWERED_EXTERNALLY_TYPE.type) {

        }
    }

    /**
     * 모든 콜 로그를 삭제한다.
     *
     * 디바이스 로그 데이터가 완전히 삭제되면, 콜 로그 관련 LiveData 모두 초기화
     */
    fun deleteAllCallLog() {
        callBasicLocalDataSource.deleteAllCallLog().let { result ->
            if (result) {
                _callLogList.value?.clear()
                _incomingCallLogList.value?.clear()
                _outgoingCallLogList.value?.clear()
                _missedCallLogList.value?.clear()
            }
        }
    }

    /**
     * 특정 콜 로그를 삭제한다.
     *
     * @param logIdList 지우려하는 콜로그 ID 리스트
     *
     * 디바이스 로그 데이터가 완전히 삭제되면,
     */
    fun deleteCallLog(logIdList: ArrayList<String>) {
        callBasicLocalDataSource.deleteCallLog(logIdList).let { result ->
            if (result) {
                getAllCallLog()
            }
        }
    }
}