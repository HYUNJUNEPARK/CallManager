package com.module.simmanager.util

import android.util.Log
import com.module.simmanager.BuildConfig

object LogUtil {
    private const val MODULE_NAME = "SimManager"

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

    fun printStackTrace(e: Exception) {
        if(BuildConfig.DEBUG) {
            e.printStackTrace()
        }
    }
}