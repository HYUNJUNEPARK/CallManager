package com.module.callex.data

import android.content.Context
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import com.module.callex.data.model.contact.ContactItem
import com.module.callex.data.model.contact.ContactList
import com.module.callex.data.model.log.CallLogItem
import com.module.callex.data.model.log.CallLogList

class CallBasicLocalDataSource(private val context: Context) {
//전화 기록 관련 API
    /**
     * 디바이스의 통화기록을 모두 가져온다.
     *
     * @return ArrayList<CallLogItem?>
     */
    fun getAllCallLog(): CallLogList? {
        try {
            val uri = CallLog.Calls.CONTENT_URI
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            val numberIdx = cursor?.getColumnIndex(CallLog.Calls.NUMBER)
            val typeIdx = cursor?.getColumnIndex(CallLog.Calls.TYPE)
            val dateIdx = cursor?.getColumnIndex(CallLog.Calls.DATE)
            val durationIdx = cursor?.getColumnIndex(CallLog.Calls.DURATION)

            val callLogList = CallLogList()

            while (cursor?.moveToNext() == true) {
                val number = cursor.getString(numberIdx!!)
                val type = cursor.getString(typeIdx!!)
                val date = cursor.getString(dateIdx!!)
                val duration = cursor.getString(durationIdx!!)

                callLogList.add(CallLogItem(number, type, date, duration))
            }
            cursor?.close()

            return callLogList
        } catch (e: NullPointerException) {

        } catch (e: SecurityException) {

        } catch (e: Exception) {

        }
        return null
    }

    fun deleteCallLog() {

    }

    fun getIncomingCallLog() {

    }

    fun getOutgoingCallLog() {

    }

//연락처 관련 API
    /**
     * 디바이스에 저장된 연락처를 가져온다.
     *
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

        } catch (e: SecurityException) {

        } catch (e: Exception) {

        }
        return null
    }

    fun deleteContact() {

    }
}