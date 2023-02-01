package com.module.simmanager.ui

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
import com.module.simmanager.model.ActivatedSimItem
import com.module.simmanager.model.ActivatedSimList
import com.module.simmanager.model.SimItem
import com.module.simmanager.model.SimList
import com.module.simmanager.util.SimManagerConst.NOT_SERVICEABLE
import com.module.simmanager.util.SimManagerConst.RESTRICTED_ZONE_SERVICE

class SimViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    //private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

    //디바이스 모든 USIM, eSIM 리스트(eSIM 의 경우 사용자 설정에 따라서 데이터가 없을 수 있음)
    private var _allSIMList = MutableLiveData<SimList>()
    val allSIMList : LiveData<SimList>
        get() = _allSIMList

    //eSIM 디바이스 모델용(API 28 이상부터 사용 권장)
    //eSIM을 지원하는 디바이스 모델의 경우 사용자가 USIM ON/OFF 를 설정할 수 있기 때문에 활성화된 USIM, eSIM 만 필터링할 필요가 있다.
    private var _activatedSIMList = MutableLiveData<ActivatedSimList>()
    val activatedSIMList : LiveData<ActivatedSimList>
        get() = _activatedSIMList

    /**
     * 디바이스 모든 USIM, eSIM 리스트를 가져온다.
     */
    fun getAllSimList() {
        try {
            //권한이 없는 경우
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
                return
            }

            //권한이 있는 경우
            val simList = SimList()
            val activeSubscriptionInfoList = subscriptionManager.activeSubscriptionInfoList

            for (sim in activeSubscriptionInfoList.iterator()) {
                //eSIM 지원 모델
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val simItem = SimItem(
                        number = sim.number,
                        isEmbedded = sim.isEmbedded,
                        carrierName = sim.carrierName,
                        simSlotIndex = sim.simSlotIndex
                    )
                    simList.add(simItem)
                } else {
                    //eSIM 없는 모델
                    val simItem = SimItem(
                        number = sim.number,
                        isEmbedded = false,
                        carrierName = sim.carrierName,
                        simSlotIndex = sim.simSlotIndex
                    )
                    simList.add(simItem)
                }
            }
            _allSIMList.value = simList
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
     * 활성화된 SIM 리스트를 가져온다.
     * eSIM 지원 디바이스에서 필요 시 사용한다.(API 28 이상부터 사용)
     */
    fun getActivatedSimList() {
        try {
            //TODO 동일한 if문 두개 ? 더 깔끔하게 표현할 수 있는 방법 생각해 볼 것
            //심 리스트 캐시 데이터가 비어있는지 확인하고 비어있다면, 캐시 데이터 초기화
            if (allSIMList.value == null) {
                getAllSimList()
            }

            //권한 설정이나 디바이스에 심이 없는 경우 등 캐시 데이터가 초기화되지 않으며 동작 정지
            if (allSIMList.value == null) {
                return
            }

            val activatedSimList = ActivatedSimList()

            for (sim in allSIMList.value!!.iterator()) {
                //TODO CharSequence 와 String 을 비교 중 문제 없는지 확인할 것
                //TODO sim? nullable 처리가 불안함
                if (sim?.carrierName != RESTRICTED_ZONE_SERVICE && sim?.carrierName != NOT_SERVICEABLE) {
                    val activatedSimItem = ActivatedSimItem(
                        number = sim?.number,
                        isEmbedded = sim?.isEmbedded,
                        simSlotIndex = sim?.simSlotIndex
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