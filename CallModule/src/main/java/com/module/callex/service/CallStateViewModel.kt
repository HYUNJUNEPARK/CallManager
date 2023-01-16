package com.module.callex.service

import android.telecom.Call
import android.telecom.Call.*
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CallStateViewModel {

    var call: Call? = null
        set(value) {
            field?.unregisterCallback(callback)
            value?.let {
                it.registerCallback(callback)
                //state.onNext(it.state)
            }
            field = value
        }

    //TODO LiveData ???
    var callState = MutableLiveData<Int>()
    val uiCallState: LiveData<Int>
        get() = callState

    /*
        val incomingCallLogList: LiveData<CallLogList>
        get() = _incomingCallLogList
     */

    private val callback = object : Call.Callback() {
        override fun onStateChanged(call: Call, newState: Int) {
            callState.value = newState

            when (newState) {
                STATE_DIALING -> {
                    Log.d("testLog", "1: $newState")
                    println("uiCallState : ${uiCallState.value}")
                }
                //전화 올 때
                STATE_RINGING -> {
                    Log.d("testLog", "2: $newState")
                    println("uiCallState : ${uiCallState.value}")
                }
                STATE_HOLDING -> {
                    Log.d("testLog", "3: $newState")
                    println("uiCallState : ${uiCallState.value}")
                }
                STATE_ACTIVE -> {
                    Log.d("testLog", "4: $newState")
                    println("uiCallState : ${uiCallState.value}")
                }
                STATE_DISCONNECTED -> {
                    Log.d("testLog", "7: $newState")
                    println("uiCallState : ${uiCallState.value}")
                }
                STATE_SELECT_PHONE_ACCOUNT -> {
                    Log.d("testLog", "8: $newState")
                    println("uiCallState : ${uiCallState.value}")
                }
                STATE_CONNECTING -> {
                    Log.d("testLog", "9: $newState")
                    println("uiCallState : ${uiCallState.value}")
                }
                STATE_DISCONNECTING -> {
                    Log.d("testLog", "10: $newState")
                    println("uiCallState : ${uiCallState.value}")
                }
            }
            //Timber.d(call.toString())
            //state.onNext(newState)
        }
    }



}