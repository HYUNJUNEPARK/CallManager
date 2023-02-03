package com.module.callmanager.data

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.module.callmanager.model.contact.ContactItem
import com.module.callmanager.model.contact.ContactList
import com.module.callmanager.util.LogUtil

class ContactLocalDataSource(private val context: Context) {
    /**
     * 디바이스에 저장된 연락처를 가져온다.
     * @return ContactList : ArrayList<ContactItem?>
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
            LogUtil.printStackTrace(e)
        } catch (e: SecurityException) {
            LogUtil.printStackTrace(e)
        } catch (e: Exception) {
            LogUtil.printStackTrace(e)
        }
        return null
    }

    /**
     * 디바이스의 특정 연락처를 지운다.
     */
    fun deleteContact() {
        try {

        } catch (e: NullPointerException) {
            LogUtil.printStackTrace(e)
        } catch (e: SecurityException) {
            LogUtil.printStackTrace(e)
        } catch (e: Exception) {
            LogUtil.printStackTrace(e)
        }
    }

    /**
     * 디바이스의 모든 연락처를 지운다.
     */
    fun deleteAllContact() {
        try {

        } catch (e: NullPointerException) {
            LogUtil.printStackTrace(e)
        } catch (e: SecurityException) {
            LogUtil.printStackTrace(e)
        } catch (e: Exception) {
            LogUtil.printStackTrace(e)
        }
    }
}