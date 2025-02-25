package com.lixd.autolark.kit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.lixd.autolark.task.AlarmClockTaskManager.Companion.ALARM_CLOCK_TYPE
import com.lixd.autolark.task.WakeAppTaskManager

//上班
class StartAlarmClockReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(
            "AlarmClockReceiver-start",
            "onReceive 闹钟回调了"
        )
        WakeAppTaskManager.instance.startTask(true)
    }
}

//下班
class EndAlarmClockReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(
            "AlarmClockReceiver-end",
            "onReceive 闹钟回调"
        )
        WakeAppTaskManager.instance.startTask(false)
    }
}