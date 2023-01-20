package com.module.callex.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.telecom.Call
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.telecom.VideoProfile
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.module.callex.util.CallManagerConst.CALL_OUTGOING
import com.module.callex.util.CallManagerConst.INTENT_KEY_CALL_STATE
import com.module.callex.util.CallManagerConst.REQUEST_PERMISSION

class CallViewModel(application: Application): AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

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
     * @param phoneNumber
     */
    fun makeCall(phoneNumber: String) {
        if (PermissionChecker.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PermissionChecker.PERMISSION_GRANTED) {
            val uri = "tel:$phoneNumber".toUri()
            val intent = Intent(Intent.ACTION_CALL, uri)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(INTENT_KEY_CALL_STATE, CALL_OUTGOING)
            context.startActivity(intent)
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_PERMISSION
            )
        }
    }

    fun answerCall() {
        call!!.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    fun disconnectCall() {
        call!!.disconnect()
    }
}