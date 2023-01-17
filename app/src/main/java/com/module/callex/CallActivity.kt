package com.module.callex

import android.os.Bundle
import android.os.Handler
import android.telecom.Call
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.module.callex.databinding.ActivityCallBinding
import com.module.callex.ui.CallViewModel
import com.module.callex.ui.CallViewModel.Companion.uiCallState

class CallActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCallBinding
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_call)
        binding.callViewModel = CallViewModel()
        binding.activity = this
        handler = Handler() //TODO Instead, use an java.util.concurrent.Executor

        callStateObserver()
    }

    private fun callStateObserver() {
        uiCallState.observe(this) { callState ->
            when(callState) {
                Call.STATE_DIALING -> {
                    binding.callState = "전화 거는 중"
                }
                //전화 올 때
                Call.STATE_RINGING -> {
                    binding.callState = "2"
                }
                Call.STATE_HOLDING -> {
                    binding.callState = "STATE_HOLDING"
                }
                Call.STATE_ACTIVE -> {
                    binding.callState = "통화 중"
                }
                Call.STATE_DISCONNECTED -> {
                    binding.callState = "통화 종료"
                    //TODO 더 깔끔한 표현 방식 필요
                    val task = Runnable { finish() }
                    handler.postDelayed(
                        task,
                        2000
                    )
                }
                Call.STATE_SELECT_PHONE_ACCOUNT -> {
                    binding.callState = "STATE_SELECT_PHONE_ACCOUNT"
                }
                Call.STATE_CONNECTING -> {
                    binding.callState = "STATE_CONNECTING"
                }
                Call.STATE_DISCONNECTING -> {
                    binding.callState = "STATE_DISCONNECTING"
                }
                else -> {
                    binding.callState = "Exception"
                }
            }
        }
    }
}