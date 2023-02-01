package com.module.callmanager.data

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.module.callmanager.model.contact.ContactItem
import com.module.callmanager.model.contact.ContactList

class ContactLocalDataSource(private val context: Context) {
    /**
     * 디바이스에 저장된 연락처를 가져온다.
     * @return ArrayList<ContactItem?>
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
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 디바이스의 특정 연락처를 지운다.
     */
    fun deleteContact() {
        try {

        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 디바이스의 모든 연락처를 지운다.
     */
    fun deleteAllContact() {
        try {

        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}