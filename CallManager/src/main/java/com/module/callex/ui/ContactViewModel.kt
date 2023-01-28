package com.module.callex.ui

import android.Manifest
import android.app.Activity
import android.app.Application
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.module.callex.data.ContactLocalDataSource
import com.module.callex.model.contact.ContactList
import com.module.callex.util.CallManagerConst

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val contactLocalDataSource = ContactLocalDataSource(context)

    //디바이스 연락처 데이터 리스트
    private var _contactList = MutableLiveData<ContactList>()
    val contactList: LiveData<ContactList>
        get() = _contactList

    /**
     * 디바이스 모든 연락처를 가져온다.
     * 가져온 데이터는 ContactList 클래스 멤버 변수 contactList 에 초기화
     */
    fun getContacts() {
        //연락처 접근 권한이 없는 경우
        if(PermissionChecker.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PermissionChecker.PERMISSION_DENIED) {
            return
        }

        //연락처 접근 권한이 있는 경우
        _contactList.value = contactLocalDataSource.getContacts()
        println("getContacts() contactList(LiveData) : ${contactList.value}")

    }
}