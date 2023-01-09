package com.module.callex

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.module.callex.model.ContactItem
import com.module.callex.model.ContactList

class Calls(context: Context) {
    private val context = context

//전화 관련 API
    fun getAllCallLog() {

    }

    fun getIncomingCallLog() {

    }

    fun getOutgoingCallLog() {

    }

//연락처 관련 API
    /**
     * 디바이스에 저장된 연락처를 가져온다.
     */
    fun getContacts(): ContactList? {
        try {
            val uri : Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

            val contactInfo = arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )

            val cursor = context.contentResolver.query(uri, contactInfo, null, null, null)

            val contactList = ContactList()

            while (cursor?.moveToNext() == true) {
                val id = cursor.getString(0)
                val name = cursor.getString(1)
                val number = cursor.getString(2)

                contactList.add(ContactItem(id, name, number))
            }
            cursor?.close()

            return contactList
        } catch (e: NullPointerException) {
            //e.printStackTrace()
        } catch (e: SecurityException) {
            //e.printStackTrace()
        } catch (e: Exception) {
            //e.printStackTrace()
        }
        return null
    }
}