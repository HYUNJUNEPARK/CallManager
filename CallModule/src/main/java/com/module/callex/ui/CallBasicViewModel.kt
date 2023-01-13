package com.module.callex.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.module.callex.data.CallBasicLocalDataSource
import com.module.callex.data.model.contact.ContactList
import com.module.callex.data.model.log.CallLogList

class CallBasicViewModel(application: Application) : AndroidViewModel(application) {
    private val callBasicLocalDataSource = CallBasicLocalDataSource(application)

    private val _contactList = MutableLiveData<ContactList>()
    val contactList: LiveData<ContactList>
        get() = _contactList

    private val _callLogList = MutableLiveData<CallLogList>()
    val callLogList: LiveData<CallLogList>
        get() = _callLogList

    fun getContacts() {
        _contactList.value = callBasicLocalDataSource.getContacts()
        println(contactList.value)
    }

    fun getCallLog() {
        _callLogList.value = callBasicLocalDataSource.getAllCallLog()
        println(callLogList.value)
    }
}