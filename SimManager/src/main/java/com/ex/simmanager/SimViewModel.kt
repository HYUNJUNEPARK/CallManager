package com.ex.simmanager

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SubscriptionManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ex.simmanager.data.model.SimItem
import com.ex.simmanager.data.model.SimList

class SimViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    //private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

    //디바이스 내부 USIM / eSIM 리스트
    private var _simList = MutableLiveData<SimList>()
    val simList : LiveData<SimList>
        get() = _simList

    /**
     * 디바이스 SIM 카드리스트를 가져온다.
     * USIM의 경우 꺼져있어도 리스트를 가져오며, eSIM의 경우 꺼져있다면 목록을 가져오지 않는다.
     */
    fun getActivatedSimList() {
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                val simList = SimList()
                val activeSubscriptionInfoList = subscriptionManager.activeSubscriptionInfoList

                for (sim in activeSubscriptionInfoList.iterator()) {
                    //API 28 이상 isEmbedded 사용 가능
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val simItem = SimItem(
                            number = sim.number,
                            isEmbedded = sim.isEmbedded
                        )
                        simList.add(simItem)
                    } else {
                        val simItem = SimItem(
                            number = sim.number,
                            isEmbedded = false
                        )
                        simList.add(simItem)
                    }
                }
                _simList.value = simList
            }
            println("simList(LiveData) : ${simList.value}")
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 디바이스 USIM / eSIM 리스트를 만든다.
     */
//    fun getSimList() {
//    }
}

//https://gooners0304.tistory.com/entry/%ED%98%84%EC%9E%AC-%EB%8B%A8%EB%A7%90%EA%B8%B0%EA%B0%80-%EB%8D%94%EB%B8%94-%EC%9C%A0%EC%8B%AC%EC%9D%BC-%EA%B2%BD%EC%9A%B0-%EC%A0%84%ED%99%94%EB%B2%88%ED%98%B8%EB%A5%BC-%EA%B0%80%EC%A0%B8%EC%98%A4%EB%8A%94-%EB%A9%94%EC%86%8C%EB%93%9C


//https://gooners0304.tistory.com/entry/USIM-%EA%B4%80%EB%A6%AC-%ED%81%B4%EB%9E%98%EC%8A%A4-%EC%B4%9D%EC%A0%95%EB%A6%AC%EC%9C%A0%EC%8B%AC-%EC%B2%B4%ED%81%AC-%EB%B2%88%ED%98%B8-%EA%B0%80%EC%A0%B8%EC%98%A4%EA%B8%B0-%EB%B2%88%ED%98%B8-%ED%8F%AC%EB%A7%B7-%ED%86%B5%EC%9D%BC

//https://it-highjune.tistory.com/18

//https://tourspace.tistory.com/450