package com.module.callex

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.module.callex.databinding.ActivityMainBinding
import com.module.callex.util.Permission

//[Android/통화 화면 바꾸기] 2. 기본 전화 앱 등록하기
//https://raon-studio.tistory.com/13?category=1055922

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var permission: Permission
    private lateinit var calls: Calls

    //기본 전화 앱으로 변경 되었는지 결과를 확인하기 위한 ActivityResultLauncher
    private val changeDefaultDialerResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (calls.isDefaultDialer) {
                //기본 앱으로 설정되었을 때
            } else {
                //기본 앱으로 설정되지 않았을 때
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permission = Permission(this)
        calls = Calls(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = CallsViewModel(application)
        binding.calls = calls
        binding.activity = this
        binding.testNumber = "01012341234"
        permission.checkPermissions()
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
        val intent = calls.createDefaultDialerIntent()
        changeDefaultDialerResultLauncher.launch(intent)
    }
}