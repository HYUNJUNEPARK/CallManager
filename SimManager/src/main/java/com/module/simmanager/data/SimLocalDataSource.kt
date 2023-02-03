package com.module.simmanager.data

import android.content.Context
import android.os.Build
import android.telephony.SubscriptionManager
import com.module.simmanager.model.SimItem
import com.module.simmanager.model.SimList
import com.module.simmanager.util.LogUtil

class SimLocalDataSource(private val context: Context) {

    /**
     * 디바이스 모든 USIM, eSIM 리스트를 반환한다.
     * @return ArrayList<SimItem>?
     */
    fun getAllSimList(): SimList? {
        try {
            val simList = SimList()
            val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
            val activeSubscriptionInfoList = subscriptionManager.activeSubscriptionInfoList

            for (sim in activeSubscriptionInfoList.iterator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { //eSIM 지원 모델
                    val simItem = SimItem(
                        number = sim.number,
                        isEmbedded = sim.isEmbedded,
                        carrierName = sim.carrierName,
                        simSlotIndex = sim.simSlotIndex
                    )
                    simList.add(simItem)
                } else { //eSIM 없는 모델
                    val simItem = SimItem(
                        number = sim.number,
                        isEmbedded = false,
                        carrierName = sim.carrierName,
                        simSlotIndex = sim.simSlotIndex
                    )
                    simList.add(simItem)
                }
            } //for

            return simList
        } catch (e: SecurityException) {
            LogUtil.printStackTrace(e)
        } catch (e: NullPointerException) {
            LogUtil.printStackTrace(e)
        } catch (e: Exception) {
            LogUtil.printStackTrace(e)
        }
        return null
    }
}