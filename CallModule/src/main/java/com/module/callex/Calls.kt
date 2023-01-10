package com.module.callex

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.telecom.TelecomManager
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.core.net.toUri

/**
 *
 * <uses-permission android:name="android.permission.READ_CONTACTS"/>
 * <uses-permission android:name="android.permission.CALL_PHONE"/>
 *
 */
class Calls(private val context: Context) {
    companion object {
        const val REQUEST_PERMISSION = 0
    }

//전화 관련 API
    /**
     * 앱에 통화 권한이 있다면 디폴트 앱에 파라미터로 번호를 전달
     *
     * @param phoneNumber
     */
    fun makeCall(phoneNumber: String) {
        if (PermissionChecker.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) == PermissionChecker.PERMISSION_GRANTED
        ) {
            val uri = "tel:$phoneNumber".toUri()

            context.startActivity(Intent(Intent.ACTION_CALL, uri))
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_PERMISSION)
        }
    }

//    /**
//     *
//     */
//    fun offerReplacingDefaultDialer() {
//
//        if (context.getSystemService(TelecomManager::class.java).defaultDialerPackage != context.packageName) {
//            Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
//                .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
//                .let(::startActivity)
//        }
//    }

    fun makeVideoCall() {

    }

    fun receiveCall() {

    }

    fun denyCall() {

    }

    fun recordCall() {

    }
}