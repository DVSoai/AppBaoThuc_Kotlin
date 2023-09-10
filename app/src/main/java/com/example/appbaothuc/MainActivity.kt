package com.example.appbaothuc

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TimePicker
import com.example.appbaothuc.broadcast.Receiver
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var timePicker: TimePicker
    private lateinit var repeatCheckBox: CheckBox
    private lateinit var setAlarmButton: Button
    private lateinit var selectedTimeEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timePicker = findViewById(R.id.timePicker)
        repeatCheckBox = findViewById(R.id.repeatCheckBox)
        setAlarmButton = findViewById(R.id.setAlarmButton)
        selectedTimeEditText = findViewById(R.id.selectedTimeEditText)

        setAlarmButton.setOnClickListener {
            setAlarm()
        }
    }

    private fun setAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val hour = timePicker.hour
        val minute = timePicker.minute
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        val intent = Intent(this, Receiver::class.java)
        intent.putExtra("repeat", repeatCheckBox.isChecked)

        // Sử dụng requestCode duy nhất dựa trên thời gian hiện tại để tránh xung đột
        val requestCode = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Xác định khoảng thời gian giữa các lần báo thức (millis) - lặp lại hàng ngày
        val intervalMillis = AlarmManager.INTERVAL_DAY

        // Đặt báo thức lặp lại hàng ngày
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            intervalMillis,
            pendingIntent
        )

        // Hiển thị giờ đã chọn trên EditText
        val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
        selectedTimeEditText.setText(formattedTime)
    }
}
