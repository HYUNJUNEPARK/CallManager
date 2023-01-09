package com.module.callex

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.module.callex.model.ContactList

//TODO ViewMOdel Scope

class CallsViewModel(application: Application) : AndroidViewModel(application) {
    private val calls = Calls(application)

    private val _contactList = MutableLiveData<ContactList>()
    val contactList: LiveData<ContactList>
        get() = _contactList

    fun getContacts() {
        _contactList.value = calls.getContacts()
    }
}