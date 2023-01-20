package com.module.callex.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telecom.Call
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ex.simmanager.ui.SimViewModel
import com.module.callex.util.Permission
import com.module.callex.R
import com.module.callex.data.model.log.LogType
import com.module.callex.databinding.ActivityMainBinding
import com.module.callex.ui.CallViewModel.Companion.uiCallState
import com.module.callex.util.CallModuleConst.CALL_OUTGOING
import com.module.callex.util.CallModuleConst.INTENT_KEY_CALL_STATE
import com.module.callex.util.CallUtil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var permission: Permission
    private lateinit var callUtil: CallUtil

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permission = Permission(this)
        callUtil = CallUtil(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.callLogViewModel = CallLogViewModel(application)
        binding.contactViewModel = ContactViewModel(application)
        binding.callViewModel = CallViewModel(application)
        binding.simViewModel = SimViewModel(application)
        binding.callUtil = callUtil
        binding.main = this
        binding.logType = LogType.OUTGOING //테스트 파라미터

        permission.checkPermissions()
        callStateObserver()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.all { it ==  PackageManager.PERMISSION_GRANTED}) {
            permission.permissionGranted()
        } else {
            permission.permissionDenied()
        }
    }

    fun changeDefaultDialer() {
        val intent = callUtil.createDefaultDialerIntent()
        changeDefaultDialerResultLauncher.launch(intent)
    }

    //기본 전화 앱으로 변경 되었는지 결과를 확인하기 위한 ActivityResultLauncher
    private val changeDefaultDialerResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (callUtil.isDefaultDialer) {
                //기본 앱으로 설정되었을 때
            } else {
                //기본 앱으로 설정되지 않았을 때
            }
        }

    private fun callStateObserver() {
        uiCallState.observe(this) { callState ->
            if (callState == Call.STATE_DIALING) {
                val intent = Intent(this, CallActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(INTENT_KEY_CALL_STATE , CALL_OUTGOING)
                startActivity(intent)
            }
        }
    }
}