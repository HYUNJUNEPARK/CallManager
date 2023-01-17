package com.module.callex.ui

import android.telecom.Call
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CallViewModel: ViewModel() {
    companion object {
        internal var callState = MutableLiveData<Int>() //콜 모듈에서만 접근 가능하도록 internal 추가
        val uiCallState: LiveData<Int>
            get() = callState
    }

    //TODO 키워드 이해 필요. internal 추가 ?
    var call: Call? = null
        set(value) {
            field?.unregisterCallback(callback)
            value?.let {
                it.registerCallback(callback)
                //state.onNext(it.state)
            }
            field = value
        }

    //TODO Call.Callback 이해 필요
    private val callback = object : Call.Callback() {
        override fun onStateChanged(call: Call, newState: Int) {
            callState.value = newState
        }
    }

    fun answer() {

    }

    fun hangup() {

    }
}