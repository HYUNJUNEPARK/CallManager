package com.module.callex.service

import android.content.Intent
import android.os.Build
import android.telecom.Call
import android.telecom.InCallService
import com.module.callex.ui.CallActivity
import com.module.callex.ui.CallViewModel
import com.module.callex.util.CallManagerConst.CALL_INCOMING
import com.module.callex.util.CallManagerConst.INTENT_KEY_CALL_STATE

class CallService : InCallService() {
    override fun onCallAdded(call: Call) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            CallViewModel.call = call
            val state = call.details.state //SDK 31 이상
            CallViewModel.callState.value = state

            //전화 수신 시
            if (state == Call.STATE_RINGING) {
                val intent = Intent(this, CallActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(INTENT_KEY_CALL_STATE, CALL_INCOMING)
                startActivity(intent)
            }
        } else {
            //TODO SDK 31 이하
            //TODO CallActivity onDestroy() 부분 함께 해결할 것
        }
    }

    override fun onCallRemoved(call: Call) {
        CallViewModel.call = null
    }
}