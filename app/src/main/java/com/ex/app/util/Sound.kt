package com.ex.app.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity

class Sound(private val context: Context) {
    private val vibrator = context.getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator

    //디바이스 모드 식별 -> 무음? 진동? 소리?
    fun checkDeviceMode() {

    }

    //통화 진동 온
    fun ringByCall() {
        //진동 모드 일 때
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createWaveform(
                longArrayOf(500, 1000, 500, 2000),
                intArrayOf(0, 50, 0, 200),
                0 //-1 한번만, 0 무한
            )
            vibrator.vibrate(vibrationEffect)
        }
        else {
            vibrator.vibrate(
                longArrayOf(500, 1000, 500, 2000),
                0
            )
        }

        //무음 모드 일 때

        //콜 모드 일 때

    }

    //통화 진동 오프
    fun offRing() {
        vibrator.cancel()
    }

    //메시지 진동
    fun vibrateByMessage() {

    }
}