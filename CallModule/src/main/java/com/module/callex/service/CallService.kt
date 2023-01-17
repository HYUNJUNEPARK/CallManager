package com.module.callex.service

import android.os.Build
import android.telecom.Call
import android.telecom.InCallService
import android.util.Log
import com.module.callex.ui.CallViewModel
import com.module.callex.ui.CallViewModel.Companion.callState
import com.module.callex.util.CallUtil.Companion.TAG

class CallService : InCallService() {
    //private val callViewModel = CallViewModel()

    override fun onCallAdded(call: Call) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.d(TAG, "onCallAdded $call")
            CallViewModel.call = call
            callState.value = call.details.state
        } else {
            //TODO SDK 31 이하
        }
    }

    override fun onCallRemoved(call: Call) {
        Log.d(TAG, "onCallRemoved $call")
        CallViewModel.call = null
    }
}