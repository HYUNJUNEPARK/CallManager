package com.module.simmanager.ui

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SubscriptionManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.module.simmanager.R
import com.module.simmanager.model.ActivatedSimItem
import com.module.simmanager.model.ActivatedSimList
import com.module.simmanager.model.SimItem
import com.module.simmanager.model.SimList
import com.module.simmanager.util.LogUtil
import com.module.simmanager.util.SimManagerConst.NOT_SERVICEABLE
import com.module.simmanager.util.SimManagerConst.RESTRICTED_ZONE_SERVICE

class SimViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    //디바이스 모든 USIM, eSIM 리스트(eSIM 의 경우 사용자 설정에 따라서 데이터가 없을 수 있음)
    private var _allSIMList = MutableLiveData<SimList>()
    val allSIMList : LiveData<SimList>
        get() = _allSIMList

    //API 28 이상 eSIM 디바이스 모델용
    //eSIM을 지원하는 디바이스 모델의 경우 사용자가 USIM ON/OFF 를 설정할 수 있기 때문에 활성화된 USIM, eSIM 만 필터링할 필요가 있다.
    private var _activatedSIMList = MutableLiveData<ActivatedSimList>()
    val activatedSIMList : LiveData<ActivatedSimList>
        get() = _activatedSIMList

    /**
     * 디바이스 모든 USIM, eSIM 리스트를 가져와 allSIMList 캐시 데이터를 초기화한다.
     */
    fun getAllSimList() {
        try {
            //권한이 없는 경우
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
                LogUtil.logE(context.getString(R.string.permission_denied_read_phone_state))
                return
            }

            //권한이 있는 경우
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

            _allSIMList.value = simList
            LogUtil.logD("allSIMList(LiveData) : ${allSIMList.value}")
        } catch (e: SecurityException) {
            LogUtil.printStackTrace(e)
        } catch (e: NullPointerException) {
            LogUtil.printStackTrace(e)
        } catch (e: Exception) {
            LogUtil.printStackTrace(e)
        }
    }

    /**
     * 활성화된 SIM 리스트를 가져와 activatedSIMList 캐시 데이터를 초기화한다.
     * API 28 이상 eSIM 지원 디바이스에서 사용
     */
    fun getActivatedSimList() {
        try {
            //TODO 동일한 if문 두개 : 더 깔끔하게 표현할 수 있는 방법 생각해 볼 것
            //심 리스트 캐시 데이터가 비어있는지 확인하고 비어있다면, 캐시 데이터 초기화
            if (allSIMList.value == null) {
                getAllSimList()
            }

            //권한 설정이나 디바이스에 심이 없는 경우 등의 이유로 캐시 데이터가 초기화되지 않았다면, 동작 정지
            if (allSIMList.value == null) {
                LogUtil.logE(context.getString(R.string.does_not_init_allSIMList_cache))
                return
            }

            val activatedSimList = ActivatedSimList()
            
            //TODO carrierName 에 RESTRICTED_ZONE_SERVICE, NOT_SERVICEABLE 외 더 많은 상황이 있을 수 있음
            for (sim in allSIMList.value!!.iterator()) {
                if (sim?.carrierName != RESTRICTED_ZONE_SERVICE && sim?.carrierName != NOT_SERVICEABLE) {
                    val activatedSimItem = ActivatedSimItem(
                        number = sim?.number,
                        isEmbedded = sim?.isEmbedded,
                        simSlotIndex = sim?.simSlotIndex
                    )
                    activatedSimList.add(activatedSimItem)
                }
            } //for

            _activatedSIMList.value = activatedSimList
            LogUtil.logD("activatedSimList(LiveData) : ${activatedSIMList.value}")
        } catch (e: SecurityException) {
            LogUtil.printStackTrace(e)
        } catch (e: NullPointerException) {
            LogUtil.printStackTrace(e)
        } catch (e: Exception) {
            LogUtil.printStackTrace(e)
        }
    }
}