package com.module.callex.service

import android.os.Build
import android.telecom.Call
import android.telecom.InCallService
import android.util.Log
import androidx.annotation.RequiresApi
import com.module.callex.ui.CallStateViewModel
import com.module.callex.ui.CallStateViewModel.Companion.callState

class CallService : InCallService() {
    private val viewModel = CallStateViewModel()

    //TODO SDK 31 이하일 때 처리 필요
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCallAdded(call: Call) {
        viewModel.call = call
        callState.value = call.details.state
    }

    override fun onCallRemoved(call: Call) {
        Log.d("testLog", "removed: $call")
    }
}