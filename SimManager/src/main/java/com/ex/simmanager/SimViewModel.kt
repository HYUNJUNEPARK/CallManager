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
import com.ex.simmanager.data.model.activated.ActivatedSimItem
import com.ex.simmanager.data.model.activated.ActivatedSimList
import com.ex.simmanager.data.model.sim.SimItem
import com.ex.simmanager.data.model.sim.SimList
import com.ex.simmanager.util.SimModuleConst.NOT_SERVICEABLE
import com.ex.simmanager.util.SimModuleConst.RESTRICTED_ZONE_SERVICE

class SimViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    //private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

    //디바아스 모든 USIM && eSIM 리스트(eSIM 의 경우 사용자 설정에 따라서 데이터가 없을 수 있음)
    private var _allSIMList = MutableLiveData<SimList>()
    val allSIMList : LiveData<SimList>
        get() = _allSIMList

    //eSIM 디바이스 모델용
    //eSIM을 지원하는 디바이스 모델의 경우 사용자가 USIM ON/OFF 를 설정할 수 있기 때문에 활성화된 USIM / eSIM 만 필터링할 필요가 있다.
    private var _activatedSIMList = MutableLiveData<ActivatedSimList>()
    val activatedSIMList : LiveData<ActivatedSimList>
        get() = _activatedSIMList


    fun getAllSimList() {
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                val simList = SimList()
                val activeSubscriptionInfoList = subscriptionManager.activeSubscriptionInfoList

                for (sim in activeSubscriptionInfoList.iterator()) {
                    //eSIM 지원 모델
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val simItem = SimItem(
                            number = sim.number,
                            isEmbedded = sim.isEmbedded,
                            carrierName = sim.carrierName
                        )
                        simList.add(simItem)
                    } else {
                        //eSIM 없는 모델
                        val simItem = SimItem(
                            number = sim.number,
                            isEmbedded = false,
                            carrierName = sim.carrierName
                        )
                        simList.add(simItem)
                    }
                }
                _allSIMList.value = simList
            }
            println("allSIMList(LiveData) : ${allSIMList.value}")
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 활성화된 SIM 스트를 가져온다.
     * eSIM 지원 디바이스에서 필요 시 사용한다.
     */
    fun getActivatedSimList() {
        try {
            if (allSIMList.value == null) {
                getAllSimList()
            }
            val activatedSimList = ActivatedSimList()

            for (sim in allSIMList.value!!.iterator()) {
                if (sim.carrierName != RESTRICTED_ZONE_SERVICE && sim.carrierName != NOT_SERVICEABLE) {
                    val activatedSimItem = ActivatedSimItem(
                        number = sim.number,
                        isEmbedded = sim.isEmbedded
                    )
                    activatedSimList.add(activatedSimItem)
                }
            }
            _activatedSIMList.value = activatedSimList
            println("activatedSimList(LiveData) : ${activatedSIMList.value}")
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

//안드로이드 현재 단말기가 더블 유심일 경우 전화번호를 가져오는
//https://gooners0304.tistory.com/entry/%ED%98%84%EC%9E%AC-%EB%8B%A8%EB%A7%90%EA%B8%B0%EA%B0%80-%EB%8D%94%EB%B8%94-%EC%9C%A0%EC%8B%AC%EC%9D%BC-%EA%B2%BD%EC%9A%B0-%EC%A0%84%ED%99%94%EB%B2%88%ED%98%B8%EB%A5%BC-%EA%B0%80%EC%A0%B8%EC%98%A4%EB%8A%94-%EB%A9%94%EC%86%8C%EB%93%9C

//USIM 관리 클래스 총정리(유심 체크, 번호 가져오기, 번호 포맷 통일)
//https://gooners0304.tistory.com/entry/USIM-%EA%B4%80%EB%A6%AC-%ED%81%B4%EB%9E%98%EC%8A%A4-%EC%B4%9D%EC%A0%95%EB%A6%AC%EC%9C%A0%EC%8B%AC-%EC%B2%B4%ED%81%AC-%EB%B2%88%ED%98%B8-%EA%B0%80%EC%A0%B8%EC%98%A4%EA%B8%B0-%EB%B2%88%ED%98%B8-%ED%8F%AC%EB%A7%B7-%ED%86%B5%EC%9D%BC

//https://it-highjune.tistory.com/18

//https://tourspace.tistory.com/450