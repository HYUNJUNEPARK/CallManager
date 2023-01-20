package com.module.callex.util

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.telecom.TelecomManager
import androidx.appcompat.app.AppCompatActivity

class CallAppConfig(private val context: Context) {
    //현재 앱이 기본 전화앱으로 설정되어있는지 확인해준다.
    val isDefaultDialer
        get() = _isDefaultDialer
    private val _isDefaultDialer: Boolean by lazy {
        context.packageName.equals((context.getSystemService(AppCompatActivity.TELECOM_SERVICE) as TelecomManager).defaultDialerPackage)
    }

    /**
     * 현재 앱을 기본으로 설정할 수 있는 Action 호출 Intent 를 반환한다.
     * 사용법 : Intent 를 호출할 Activity 에 ActivityResultLauncher 를 세팅한다.
     * @return Intent
     */
    fun createDefaultDialerIntent(): Intent? {
        val roleManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.getSystemService(AppCompatActivity.ROLE_SERVICE) as RoleManager
            } else {
                null
            }

        val intent = if (_isDefaultDialer) {
            //1.이미 기본 전화 앱으로 등록되어 있는 경우
                Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
            } else {
                //2. 기본 전화 앱으로 등록되어 있지 않은 경우
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //2.1 Q버전 이상에서 RoleManager 로 Intent 생성
                    roleManager?.createRequestRoleIntent(RoleManager.ROLE_DIALER)
                } else {
                    //2.2 Q버전 미만에서 TelecomManager 에 정의된 문자열로 인텐트 생성
                    Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).apply {
                        putExtra(
                            TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
                            context.packageName
                        )
                    }
                }
            }
        return intent
    }
}