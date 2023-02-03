package com.module.callmanager.data

import android.content.Context
import android.provider.CallLog
import com.module.callmanager.model.log.CallLogItem
import com.module.callmanager.model.log.CallLogList
import com.module.callmanager.util.LogUtil

class CallLogLocalDataSource(private val context: Context) {
    /**
     * 디바이스의 콜로그를 모두 가져온다.
     * @return CallLogList : ArrayList<CallLogItem?>
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
            LogUtil.printStackTrace(e)
        } catch (e: SecurityException) {
            LogUtil.printStackTrace(e)
        } catch (e: Exception) {
            LogUtil.printStackTrace(e)
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
            LogUtil.printStackTrace(e)
        } catch (e: IndexOutOfBoundsException) {
            LogUtil.printStackTrace(e)
        } catch (e: NullPointerException) {
            LogUtil.printStackTrace(e)
        } catch (e: SecurityException) {
            LogUtil.printStackTrace(e)
        } catch (e: Exception) {
            LogUtil.printStackTrace(e)
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
            LogUtil.printStackTrace(e)
        } catch (e: SecurityException) {
            LogUtil.printStackTrace(e)
        } catch (e: Exception) {
            LogUtil.printStackTrace(e)
        }
        return false
    }
}