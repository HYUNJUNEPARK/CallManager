package com.ex.simmanager

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat


//https://gooners0304.tistory.com/entry/%ED%98%84%EC%9E%AC-%EB%8B%A8%EB%A7%90%EA%B8%B0%EA%B0%80-%EB%8D%94%EB%B8%94-%EC%9C%A0%EC%8B%AC%EC%9D%BC-%EA%B2%BD%EC%9A%B0-%EC%A0%84%ED%99%94%EB%B2%88%ED%98%B8%EB%A5%BC-%EA%B0%80%EC%A0%B8%EC%98%A4%EB%8A%94-%EB%A9%94%EC%86%8C%EB%93%9C
//https://gooners0304.tistory.com/entry/USIM-%EA%B4%80%EB%A6%AC-%ED%81%B4%EB%9E%98%EC%8A%A4-%EC%B4%9D%EC%A0%95%EB%A6%AC%EC%9C%A0%EC%8B%AC-%EC%B2%B4%ED%81%AC-%EB%B2%88%ED%98%B8-%EA%B0%80%EC%A0%B8%EC%98%A4%EA%B8%B0-%EB%B2%88%ED%98%B8-%ED%8F%AC%EB%A7%B7-%ED%86%B5%EC%9D%BC

//https://it-highjune.tistory.com/18

//https://tourspace.tistory.com/450

class UsimManager(private val context: Context) {
    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private val subscriptionManager = SubscriptionManager.from(context)

    fun isUsim(): Boolean {
        try {
            return when (telephonyManager.simState) {
                TelephonyManager.SIM_STATE_READY -> true
                else -> false
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getUsimNumber() {
        try {
            //Log.d("testLog", "readNumber: ${telephonyManager.line1Number}")
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getSubscriptionInfoList(): List<SubscriptionInfo>? {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
            return subscriptionManager.activeSubscriptionInfoList
        }
        return null
    }

    fun getPhoneNumberList(subInfoList: List<SubscriptionInfo>?): ArrayList<String> {
        val phoneNumbers = ArrayList<String>()

        if (subInfoList != null)
            for (subInfo in subInfoList) {
                phoneNumbers.add(subInfo.number)
            }

        return phoneNumbers
    }

    fun getEsimNumber() {

    //        val subscriptionInfo = subscriptionManager.getActiveSubscriptionInfo()
//        SubscriptionInfo subscriptionInfo = subscriptionMgr.getActiveSubscriptionInfoForSimSlotIndex(simIndex);
//
//        String phoneNumber = subscriptionInfo.getNumber()
    }

}