package com.module.callmanager.ui

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.telecom.Call
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.telecom.VideoProfile
import androidx.core.content.PermissionChecker
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.module.callmanager.util.CallManagerConst.CALL_OUTGOING
import com.module.callmanager.util.CallManagerConst.INTENT_KEY_CALL_STATE
import com.module.callmanager.util.SimConst.simSlotName

class CallViewModel(application: Application): AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    //TODO makeCall() 내부로 넣는 것 생각해 볼 것
    private var telecomManager: TelecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

    companion object {
        var callState = MutableLiveData<Int>()
        val uiCallState: LiveData<Int>
            get() = callState

        //TODO 코드 이해 필요
        var call: Call? = null
            set(value) {
                field?.unregisterCallback(callback)
                value?.let {
                    it.registerCallback(callback)
                }
                field = value
            }

        private val callback = object : Call.Callback() {
            override fun onStateChanged(call: Call, newState: Int) {
                callState.value = newState
            }
        }
    }

    /**
     * 앱에 통화 권한이 있다면 디폴트 앱에 파라미터로 번호를 전달
     * @param phoneNumber 수신 전화 번호
     */
    fun makeCall(phoneNumber: String) {
        try {
            //통화 권한이 없는 경우
            if (PermissionChecker.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PermissionChecker.PERMISSION_DENIED) {
                return
            }

            //통화 권한이 있는 경우
            val uri = "tel:$phoneNumber".toUri()
            val intent = Intent(Intent.ACTION_CALL, uri)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(INTENT_KEY_CALL_STATE, CALL_OUTGOING)
            context.startActivity(intent)
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //TODO 코드 이해 필요함
    /**
     * eSIM 지원 모델에서 통화 기능을 구현한다.
     * @param phoneNumber 수신 전화 번호
     * @param simSlotIdx sim 이 삽입된 슬롯 인덱스
     */
    fun makeCall(phoneNumber: String, simSlotIdx: Int) {
        try {
            //통화 권한이 없는 경우
            if(PermissionChecker.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PermissionChecker.PERMISSION_DENIED) {
                return
            }

            //통화 권한이 있는 경우
            val phoneAccountHandleList: List<PhoneAccountHandle> =  telecomManager.callCapablePhoneAccounts //TODO 권한 관련해 에러가 있었음
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("com.android.phone.force.slot", true)
            intent.putExtra("Cdma_Supp", true)
            for (sim in simSlotName) {
                intent.putExtra(sim, 0)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.putExtra(
                    "android.telecom.extra.PHONE_ACCOUNT_HANDLE",
                    phoneAccountHandleList[simSlotIdx]
                )
            }
            context.startActivity(intent)

        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 전화를 받는다.
     */
    fun answerCall() {
        call!!.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    /**
     * 전화를 끊는다.
     */
    fun disconnectCall() {
        call!!.disconnect()
    }
}