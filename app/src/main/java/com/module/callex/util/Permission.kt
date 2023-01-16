package com.module.callex.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat

class Permission(private val context: Context) {
    private val permissionRequestCode = 999
    private val permissionsArray: Array<String> = arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.WRITE_CALL_LOG,
        Manifest.permission.READ_PHONE_STATE
    )

    /**
     * 권한 요청이 필요한 시점에 호출해 사용한다.
     *
     * Build.VERSION_CODES.M 미만인 경우 권한 요청 코드 필요
     */
    fun checkPermissions() {
        //AOS M 버전 이상 권한 요청
        val isAllPermissionGranted: Boolean = permissionsArray.all { permission ->
            context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }
        if (isAllPermissionGranted) {
            permissionGranted()
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                permissionsArray,
                permissionRequestCode
            )
        }
    }

    /**
     * 모든 권한이 승인되었을 때 실행한다.
     */
    fun permissionGranted() {
        Toast.makeText(context, "모든 권한 승인 완료", Toast.LENGTH_SHORT).show()
    }

    /**
     * 권한이 하나라도 거절되었을 때 실행한다.
     */
    fun permissionDenied() {
        AlertDialog.Builder(context)
            .setTitle("권한 설정")
            .setMessage("권한 거절로 인해 일부기능이 제한됩니다.")
            .setPositiveButton("종료") { _, _ ->
                (context as Activity).finish()
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
        val packageUri = Uri.parse("package:${context.packageName}")
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri)
        context.startActivity(intent)
    }
}