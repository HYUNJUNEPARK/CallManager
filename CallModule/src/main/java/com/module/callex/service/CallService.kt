package com.module.callex.service

import android.os.Build
import android.telecom.Call
import android.telecom.InCallService
import android.util.Log
import androidx.annotation.RequiresApi

class CallService : InCallService() {
    private val viewModel = CallStateViewModel()

    //TODO SDK 31 이하일 때 처리
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCallAdded(call: Call) {


        val callState = call.details.state
        viewModel.call = call
        viewModel.callState.value = callState

        Log.d("testLog", "onCallAdded: $callState")

    }

    override fun onCallRemoved(call: Call) {
        Log.d("testLog", "removed: $call")
    }
}