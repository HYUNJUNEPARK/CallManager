package com.module.callex.data

import android.content.Context
import android.database.sqlite.SQLiteException
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
            val cursor = context.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                CallLog.Calls.DEFAULT_SORT_ORDER
            )

            val callLogList = CallLogList()

            val numberIdx = cursor?.getColumnIndex(CallLog.Calls.NUMBER)
            val typeIdx = cursor?.getColumnIndex(CallLog.Calls.TYPE)
            val dateIdx = cursor?.getColumnIndex(CallLog.Calls.DATE)
            val durationIdx = cursor?.getColumnIndex(CallLog.Calls.DURATION)
            val idIdx = cursor?.getColumnIndex(CallLog.Calls._ID)

            while (cursor?.moveToNext() == true) {
                callLogList.add(
                    CallLogItem(
                        id = cursor.getString(idIdx!!),
                        number = cursor.getString(numberIdx!!),
                        type = cursor.getString(typeIdx!!),
                        date = cursor.getString(dateIdx!!),
                        duration = cursor.getString(durationIdx!!))
                    )
            }

            cursor?.close()

            return callLogList
        } catch (e: SQLiteException) {
            e.printStackTrace()
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
     * 디바이스의 특정 통화기록만 지운다.
     *
     * @param logId 지우려하는 통화기록 ID
     */
    fun deleteCallLog(logId: String) {
        try {
//            val cursor = context.contentResolver.query(
//                CallLog.Calls.CONTENT_URI,
//                null,
//                null,
//                null,
//                CallLog.Calls.DEFAULT_SORT_ORDER
//            )

            //val idIdx = cursor?.getColumnIndex(CallLog.Calls._ID)

            context.contentResolver.delete(
                CallLog.Calls.CONTENT_URI,
                CallLog.Calls._ID + " =" + logId,
                null
            )

//            while (cursor?.moveToNext() == true) {
//                val id = cursor.getString(idIdx!!)
//                println("_ID = $id")
//            }

        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 디바이스의 모든 통화기록을 지운다.
     */
    fun deleteAllCallLog(): Boolean {
        try {
            context.contentResolver.delete(
                CallLog.Calls.CONTENT_URI,
                null,
                null
            )

            return true
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
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