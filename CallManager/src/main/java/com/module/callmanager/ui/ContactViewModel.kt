package com.module.callmanager.ui

import android.Manifest
import android.app.Application
import androidx.core.content.PermissionChecker
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.module.callmanager.data.ContactLocalDataSource
import com.module.callmanager.model.contact.ContactList
import com.module.callmanager.util.LogUtil

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val contactLocalDataSource = ContactLocalDataSource(context)

    //디바이스 연락처 리스트
    private var _contactList = MutableLiveData<ContactList>()
    val contactList: LiveData<ContactList>
        get() = _contactList

    /**
     * 디바이스에 저장된 연락처를 가져와 캐시 contactList 에 초기화
     */
    fun getContacts() {
        //연락처 접근 권한이 없는 경우
        if(PermissionChecker.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PermissionChecker.PERMISSION_DENIED) {
            LogUtil.logE("Manifest.permission.READ_CONTACTS : PERMISSION_DENIED")
            return
        }

        //연락처 접근 권한이 있는 경우
        _contactList.value = contactLocalDataSource.getContacts()
        LogUtil.logD("contactList(LiveData) : ${contactList.value}")
    }
}