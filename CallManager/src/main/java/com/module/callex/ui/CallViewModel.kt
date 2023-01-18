package com.module.callex.ui

import android.telecom.Call
import android.telecom.VideoProfile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CallViewModel: ViewModel() {
    companion object {
        var callState = MutableLiveData<Int>() //콜 모듈에서만 접근 가능하도록 internal 추가
        val uiCallState: LiveData<Int>
            get() = callState

        var call: Call? = null //콜 모듈에서만 접근 가능하도록 internal 추가
            set(value) {
                field?.unregisterCallback(callback)
                value?.let {
                    it.registerCallback(callback)
                    //state.onNext(it.state)
                }
                field = value
            }

        private val callback = object : Call.Callback() {
            override fun onStateChanged(call: Call, newState: Int) {
                callState.value = newState
            }
        }
    }

    fun answerCall() {
        call!!.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    fun disconnectCall() {
        call!!.disconnect()
    }
}