package com.module.callex.util.esim

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import com.module.callex.util.esim.ESimConst.simSlotName

class ESimCallManager(private val context: Context) {
    var telecomManager: TelecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

    //권한이 없다면 makeCall() SecurityException 에서 걸림
    @SuppressLint("MissingPermission")
    var phoneAccountHandleList: List<PhoneAccountHandle> =telecomManager.callCapablePhoneAccounts

    /**
     * eSIM 지원 모델에서 통화 기능을 구현한다.
     * TODO 메서드의 성격은 CallManager 모듈로 가야하나
     * TODO 심슬롯 인덱스 데이터에 SIMManager 모듈에 의존성이 있어서 애매한 상태임
     */
    fun makeCall(phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("com.android.phone.force.slot", true)
            intent.putExtra("Cdma_Supp", true)
            for (sim in simSlotName) {
                intent.putExtra(sim, 0)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.putExtra(
                    "android.telecom.extra.PHONE_ACCOUNT_HANDLE",
                    phoneAccountHandleList[1] //TODO 여기에 심 슬롯 인덱스
                )
            }
            context.startActivity(intent)
        } catch (e: SecurityException) {
            //TODO Handling Exception 'Manifest.permission.READ_PHONE_STATE'
            e.printStackTrace()
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}