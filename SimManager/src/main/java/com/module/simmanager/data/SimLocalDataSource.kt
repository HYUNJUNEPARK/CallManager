package com.module.simmanager.data

import com.module.simmanager.model.SimItem
import com.module.simmanager.util.LogUtil

class SimLocalDataSource {

    /**
     * 디바이스 모든 USIM, eSIM 리스트를 반환한다.
     * @return ArrayList<SimItem>?
     */
    fun getAllSimList(): ArrayList<SimItem?>? {
        try {

        } catch (e: Exception) {
            LogUtil.printStackTrace(e)
        }
        return null
    }
}