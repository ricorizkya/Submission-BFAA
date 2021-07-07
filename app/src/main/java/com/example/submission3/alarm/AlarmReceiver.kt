package com.example.submission3.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.submission3.R
import com.example.submission3.ui.activity.MainActivity
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_REPEATING = "RepeatingAlarm"
        const val EXTRA_MESSAGE = "ExtraMessage"
        const val EXTRA_TYPE = "ExtraType"
        private const val ID_REPEATING = 101
        private const val TIME_ALARM = "09:00"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        showAlarmNotif(context, message)
    }

    private fun showAlarmNotif(context: Context, message: String?) {
        val channelId = "rico rizkya"
        val channelName = "daily reminder"

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("Daily Reminder")
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            builder.setChannelId(channelId)
            notificationManager.createNotificationChannel(channel)
        }

        val notif = builder.build()
        notificationManager.notify(100, notif)
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Daily Reminder is Not Active", Toast.LENGTH_SHORT).show()
    }

    fun setDailyReminder(context: Context, type: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)

        val timeArray = TIME_ALARM.split(":".toRegex()).dropLastWhile {
            it.isEmpty()
        }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(context, "Daily Reminder is Active", Toast.LENGTH_SHORT).show()
    }
}