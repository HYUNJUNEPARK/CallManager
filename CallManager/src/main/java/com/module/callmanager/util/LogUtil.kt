package com.module.callmanager.util

import android.util.Log
import com.module.callmanager.BuildConfig

object LogUtil {
    private const val MODULE_NAME = "CallManager"

    fun logD(contents: String) {
        if(BuildConfig.DEBUG) {
            Log.d(MODULE_NAME, "$contents")
        }
    }

    fun logE(contents: String) {
        if(BuildConfig.DEBUG) {
            Log.e(MODULE_NAME, "$contents")
        }
    }

    fun printStackTrace(exception: Exception) {
        if(BuildConfig.DEBUG) {
            exception.printStackTrace()
        }
    }
}