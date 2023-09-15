package com.example.appbaothuc.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.appbaothuc.R
import com.example.appbaothuc.broadcast.Receiver

class Foreground : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private val channelId = "my_channel"

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.sena)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {




        if (intent?.action == "STOP_ALARM") {

            // Hủy bỏ báo thức khi bạn muốn dừng dịch vụ
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, Receiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(pendingIntent)

            stopForeground(true)
            stopSelf()
            return START_NOT_STICKY
        }

        //   kiểm tra intent và xử lý dựa trên thông tin trong intent ở đây nếu cần

        createNotificationChannel()

        val notification = createNotification()
        startForeground(1, notification)

        mediaPlayer.start()

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.release()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, Receiver::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, Foreground::class.java)
        stopIntent.action = "STOP_ALARM"
        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Báo thức đang chạy")
            .setContentText("Đã đến giờ!")
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_stop, "STOP_ALARM", stopPendingIntent)
            .setSmallIcon(R.drawable.ic_alarm)
            .setAutoCancel(true)
            .build()

        return notification
    }


}
