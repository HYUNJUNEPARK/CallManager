package com.module.callex

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.module.callex.data.model.log.LogType
import com.module.callex.databinding.ActivityMainBinding
import com.module.callex.ui.CallBasic
import com.module.callex.ui.CallBasicViewModel
import com.module.callex.util.Permission

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var permission: Permission
    private lateinit var callBasic: CallBasic

    //기본 전화 앱으로 변경 되었는지 결과를 확인하기 위한 ActivityResultLauncher
    private val changeDefaultDialerResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (callBasic.isDefaultDialer) {
                //기본 앱으로 설정되었을 때
            } else {
                //기본 앱으로 설정되지 않았을 때
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permission = Permission(this)
        callBasic = CallBasic(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = CallBasicViewModel(application)
        binding.calls = callBasic
        binding.activity = this
        binding.testNumber = "01012341234"
        permission.checkPermissions()


        binding.button5.setOnClickListener {
            CallBasicViewModel(application).getCallLog(LogType.INCOMING)
        }


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
        val intent = callBasic.createDefaultDialerIntent()
        changeDefaultDialerResultLauncher.launch(intent)
    }
}