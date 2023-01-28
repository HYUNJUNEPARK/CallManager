package com.module.callex.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.module.callex.data.CallLogLocalDataSource
import com.module.callex.data.ContactLocalDataSource
import com.module.callex.model.contact.ContactList

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val contactLocalDataSource = ContactLocalDataSource(application)

     //디바이스 연락처 데이터 리스트
    private var _contactList = MutableLiveData<ContactList>()
    val contactList: LiveData<ContactList>
        get() = _contactList

    /**
     * 디바이스 모든 연락처를 가져온다.
     * 가져온 데이터는 ContactList 클래스 멤버 변수 contactList 에 초기화된다.
     */
    fun getContacts() {
        _contactList.value = contactLocalDataSource.getContacts()
        println("getContacts() contactList(LiveData) : ${contactList.value}")
    }
}