package com.lixd.autolark.kit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.lixd.autolark.task.WakeAppTaskManager

class AlarmClockReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmClockReceiver", "onReceive 闹钟回调了")
        WakeAppTaskManager.instance.startTask()
    }
}