package com.module.callex.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.module.callex.data.CallLocalDataSource
import com.module.callex.data.model.contact.ContactList

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val callLocalDataSource = CallLocalDataSource(application)

    /**
     * 디바이스 연락처 데이터
     */
    private var _contactList = MutableLiveData<ContactList>()
    val contactList: LiveData<ContactList>
        get() = _contactList

    /**
     * 디바이스 모든 연락처를 가져온다.
     */
    fun getContacts() {
        _contactList.value = callLocalDataSource.getContacts()
        println("getContacts() contactList(LiveData) : ${contactList.value}")
    }
}