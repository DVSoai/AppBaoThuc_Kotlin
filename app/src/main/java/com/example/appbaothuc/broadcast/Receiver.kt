package com.example.appbaothuc.broadcast

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.appbaothuc.service.Foreground

class Receiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {



        val serviceIntent=Intent(context, Foreground::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.startForegroundService(serviceIntent)
        } else {
            context?.startService(serviceIntent)
        }


    }
}