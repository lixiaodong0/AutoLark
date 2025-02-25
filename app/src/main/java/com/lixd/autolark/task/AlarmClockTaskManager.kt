package com.lixd.autolark.task

import android.util.Log
import com.lixd.autolark.conf.WorkTime
import com.lixd.autolark.kit.AlarmClockKit
import com.lixd.autolark.kit.DateKit
import com.lixd.autolark.kit.EndAlarmClockReceiver
import com.lixd.autolark.kit.StartAlarmClockReceiver
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class TaskData(val workTime: WorkTime, val minute: Int)

class AlarmClockTaskManager private constructor() {

    private val dateFormat: SimpleDateFormat =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun start(
        start: TaskData,
        end: TaskData
    ) {
        Log.d("AlarmClockTaskManager", "------Task Start----")
        appendStartWorkAlarmClock(start)
        appendEndWorkAlarmClock(end)
        Log.d("AlarmClockTaskManager", "------Task End----")
    }

    fun stop() {
        Log.d("AlarmClockTaskManager", "------Task stop----")
        stopStartWorkAlarmClock()
        stopEndWorkAlarmClock()
    }

    fun appendStartWorkAlarmClock(taskData: TaskData) {
        Log.d("AlarmClockTaskManager", "appendStartWorkAlarmClock data:${taskData}")
        val calendar = DateKit.calculatePunchInTime(
            taskData.workTime.hour,
            taskData.workTime.minute,
            -taskData.minute
        )
        Log.d("AlarmClockTaskManager", "上班打卡时间:${dateFormat.format(calendar.time)}")
        AlarmClockKit.instance.setAlarmClock(
            calendar,
            cls = StartAlarmClockReceiver::class.java,
            intentRequestCode = INTENT_REQUEST_CODE_START
        )
    }

    private fun stopStartWorkAlarmClock() {
        Log.d("AlarmClockTaskManager", "stopStartWorkAlarmClock")
        AlarmClockKit.instance.cancelAlarmClock(
            StartAlarmClockReceiver::class.java,
            INTENT_REQUEST_CODE_START
        )
    }

    fun appendEndWorkAlarmClock(taskData: TaskData) {
        Log.d("AlarmClockTaskManager", "appendEndWorkAlarmClock data:${taskData}")
        val calendar = DateKit.calculatePunchInTime(
            taskData.workTime.hour,
            taskData.workTime.minute,
            taskData.minute
        )
        Log.d("AlarmClockTaskManager", "下班打卡时间:${dateFormat.format(calendar.time)}")
        AlarmClockKit.instance.setAlarmClock(
            calendar,
            cls = EndAlarmClockReceiver::class.java,
            intentRequestCode = INTENT_REQUEST_CODE_END
        )
    }

    private fun stopEndWorkAlarmClock() {
        Log.d("AlarmClockTaskManager", "stopEndWorkAlarmClock")
        AlarmClockKit.instance.cancelAlarmClock(
            EndAlarmClockReceiver::class.java,
            INTENT_REQUEST_CODE_END
        )
    }


    companion object {
        val instance by lazy { AlarmClockTaskManager() }
        const val ALARM_CLOCK_TYPE = "alarmClockType"
        private const val INTENT_REQUEST_CODE_START = 1
        private const val INTENT_REQUEST_CODE_END = 2
    }
}

