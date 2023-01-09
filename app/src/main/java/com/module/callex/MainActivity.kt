package com.module.callex

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.module.callex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val permissionRequestCode = 999
    private val permissionsArray: Array<String> = arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_PHONE_NUMBERS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.call = Calls(this)

        checkPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.all { it ==  PackageManager.PERMISSION_GRANTED}) {
            permissionGranted()
        }
        else {
            permissionDenied()
        }
    }

    /**
     * 권한 요청이 필요한 시점에 호출해 사용한다.
     *
     * Build.VERSION_CODES.M 미만인 경우 권한 요청 코드 필요
     */
    private fun checkPermissions() {
        //AOS M 버전 이상 권한 요청
        val isAllPermissionGranted: Boolean = permissionsArray.all { permission ->
            checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }
        if (isAllPermissionGranted) {
            permissionGranted()
        } else {
            ActivityCompat.requestPermissions(
                this,
                permissionsArray,
                permissionRequestCode
            )
        }
    }

    /**
     * 모든 권한이 승인되었을 때 실행한다.
     */
    private fun permissionGranted() {
        Toast.makeText(this, "모든 권한 승인 완료", Toast.LENGTH_SHORT).show()
    }

    /**
     * 권한이 하나라도 거절되었을 때 실행한다.
     */
    private fun permissionDenied() {
        AlertDialog.Builder(this)
            .setTitle("권한 설정")
            .setMessage("권한 거절로 인해 일부기능이 제한됩니다.")
            .setPositiveButton("종료") { _, _ ->
                this.finish()
            }
            .setNegativeButton("권한 설정하러 가기") { _, _ ->
                applicationInfo()
            }
            .create()
            .show()
    }

    /**
     * 권한 설정을 호출한다.
     */
    private fun applicationInfo() {
        val packageUri = Uri.parse("package:${this.packageName}")
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri)
        this.startActivity(intent)
    }
}