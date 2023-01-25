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
import com.module.callex.R
import com.module.callex.data.model.log.LogType
import com.module.callex.databinding.ActivityMainBinding
import com.module.callex.ui.CallViewModel.Companion.uiCallState
import com.module.callex.util.CallAppConfig
import com.module.callex.util.CallManagerConst.CALL_OUTGOING
import com.module.callex.util.CallManagerConst.INTENT_KEY_CALL_STATE
import com.module.callex.util.Permission

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var permission: Permission
    private lateinit var callAppConfig: CallAppConfig
    private lateinit var simViewModel: SimViewModel

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permission = Permission(this)
        callAppConfig = CallAppConfig(this)
        simViewModel = SimViewModel(application)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.callLogViewModel = CallLogViewModel(application)
        binding.contactViewModel = ContactViewModel(application)
        binding.callViewModel = CallViewModel(application)
        binding.simViewModel = simViewModel
        binding.callUtil = callAppConfig
        binding.main = this
        binding.logType = LogType.OUTGOING //테스트 파라미터

        updateUI()
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
        val intent = callAppConfig.createDefaultDialerIntent()
        changeDefaultDialerResultLauncher.launch(intent)
    }

    //기본 전화 앱으로 변경 되었는지 결과를 확인하기 위한 ActivityResultLauncher
    private val changeDefaultDialerResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (callAppConfig.isDefaultDialer) {
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

    private fun updateUI() {
        simViewModel.getActivatedSimList()
        val simList = simViewModel.activatedSIMList.value

        val simTypeSet = mutableSetOf<Boolean?>()
        for (i in simList!!.iterator()) {
            simTypeSet.add(i?.isEmbedded)
        }

        binding.isActivatedUSIM = simTypeSet.contains(false)
        binding.isActivatedESIM = simTypeSet.contains(true)
    }
}