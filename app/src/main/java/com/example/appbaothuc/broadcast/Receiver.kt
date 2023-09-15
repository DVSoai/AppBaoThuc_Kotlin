package com.example.appbaothuc.broadcast

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.appbaothuc.service.Foreground

class Receiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (context == null || intent == null) {
            return
        }

        val isRepeat = intent.getBooleanExtra("repeat", false)

        if (isRepeat) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val serviceIntent = Intent(context, Foreground::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }

            // Lấy các giá trị intervalMillis và calendarMillis từ Intent
            val intervalMillis = intent.getLongExtra("intervalMillis", 0)
            val calendarMillis = intent.getLongExtra("calendarMillis", 0)

            // Tạo PendingIntent để đặt lại báo thức
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            // Đặt lại báo thức để lặp lại
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendarMillis,
                intervalMillis,
                pendingIntent
            )
        } else {
            val serviceIntent = Intent(context, Foreground::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        }
    }
}
