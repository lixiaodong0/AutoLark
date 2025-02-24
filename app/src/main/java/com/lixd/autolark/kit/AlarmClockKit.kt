package com.lixd.autolark.kit

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.Intent
import java.util.Calendar

class AlarmClockKit private constructor() {

    private lateinit var application: Context
    private lateinit var alarmManager: AlarmManager
    fun init(context: Context) {
        application = context.applicationContext
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    fun setAlarmClock(
        calendar: Calendar,
        extra: Intent.() -> Unit = {},
        intentRequestCode: Int = 0
    ) {
        val pendingIntent = Intent(application, AlarmClockReceiver::class.java).apply {
            extra()
        }.let {
            PendingIntent.getBroadcast(application, intentRequestCode, it, FLAG_MUTABLE)
        }
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(
                calendar.timeInMillis,
                pendingIntent
            ), pendingIntent
        )
    }

    fun cancelAlarmClock(intentRequestCode: Int = 0) {
        val pendingIntent = Intent(application, AlarmClockReceiver::class.java).let {
            PendingIntent.getBroadcast(application, intentRequestCode, it, FLAG_MUTABLE)
        }
        alarmManager.cancel(pendingIntent)
    }

    companion object {
        val instance by lazy { AlarmClockKit() }
    }
}