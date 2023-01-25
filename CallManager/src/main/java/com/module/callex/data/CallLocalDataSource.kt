package com.module.callex.data

import android.content.Context
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import com.module.callex.data.model.contact.ContactItem
import com.module.callex.data.model.contact.ContactList
import com.module.callex.data.model.log.CallLogItem
import com.module.callex.data.model.log.CallLogList

//TODO 개발 진행에 따라 로그 데이터 소스와 연락처 데이터 소스를 분리해야할 수도 있음
class CallLocalDataSource(private val context: Context) {
//전화 기록 관련 API
    /**
     * 디바이스의 콜로그를 모두 가져온다.
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
     * 디바이스의 특정 콜로그만 지운다.
     * @param logIdList 지우려하는 콜로그 ID 리스트
     * @return 삭제 성공 시 true, 실패 시 false 반환
     */
    fun deleteCallLog(logIdList: ArrayList<String>): Boolean {
        try {
            for (logId in logIdList) {
                val queryString = CallLog.Calls._ID+"=" + "'" +  logId.toInt() + "'"

                context.contentResolver.delete(
                    CallLog.Calls.CONTENT_URI,
                    queryString,
                    null
                )
            }
            return true
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 디바이스의 모든 콜로그를 지운다.
     * @return 삭제 성공 시 true, 실패 시 false 반환
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