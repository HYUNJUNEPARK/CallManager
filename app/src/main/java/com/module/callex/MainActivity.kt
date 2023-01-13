package com.module.callex

import android.Manifest
import android.app.AlertDialog
import android.app.role.RoleManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.module.callex.databinding.ActivityMainBinding
import com.module.callex.util.Permission

//[Android/통화 화면 바꾸기] 2. 기본 전화 앱 등록하기
//https://raon-studio.tistory.com/13?category=1055922

class MainActivity : AppCompatActivity() {
    //API29(Q)부터 사용 가능하며 Q 버전 미만에서는 null 반환
    private val roleManager: RoleManager? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getSystemService(ROLE_SERVICE) as RoleManager
        } else {
            null
        }
    }

    private val telecomManager: TelecomManager by lazy {
        getSystemService(TELECOM_SERVICE) as TelecomManager
    }

    //현재 기본 전화 앱이 내가 만든 앱으로 등록되어 있는지 확인할 수 있는 변수
    //호출마다 기본 전화 앱인지 확인
    private val isDefaultDialer
        get() = packageName.equals(telecomManager.defaultDialerPackage)

    private val changeDefaultDialerIntent
        get() = if (isDefaultDialer) { //1.이미 기본 전화 앱으로 등록되어 있는 경우
                    Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
                } else { //2. 기본 전화 앱으로 등록되어 있지 않은 경우
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { //2.1 Q버전 이상에서 RoleManager 로 Intent 생성
                        roleManager!!.createRequestRoleIntent(RoleManager.ROLE_DIALER)
                    } else { //2.2 Q버전 미만에서 TelecomManager 에 정의된 문자열로 인텐트 생성
                        Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).apply {
                            putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
                        }
                    }
                }

    //기본 전화 앱으로 변경 되었는지 결과를 확인하기 위한 콜백 변수
    private val changeDefaultDialerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            println("$isDefaultDialer")
        }


    private lateinit var binding: ActivityMainBinding
    private lateinit var permission: Permission

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = CallsViewModel(application)
        binding.calls = Calls(this)
        binding.testNumber = "01012341234"
        permission = Permission(this)

        permission.checkPermissions()

        binding.button3.setOnClickListener {
            changeDefaultDialerLauncher.launch(changeDefaultDialerIntent)
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
}