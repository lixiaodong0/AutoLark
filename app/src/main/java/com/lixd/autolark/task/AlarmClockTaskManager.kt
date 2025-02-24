package com.lixd.autolark.task

import android.util.Log
import com.lixd.autolark.conf.WorkTime
import com.lixd.autolark.kit.AlarmClockKit
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

    private fun appendStartWorkAlarmClock(taskData: TaskData) {
        Log.d("AlarmClockTaskManager", "appendStartWorkAlarmClock data:${taskData}")
        val currentTimeMillis = System.currentTimeMillis()

        //现在的时间
        val now = Calendar.getInstance()

        //上班的时间
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = currentTimeMillis
            set(Calendar.HOUR_OF_DAY, taskData.workTime.hour)
            set(Calendar.MINUTE, taskData.workTime.minute)
            set(Calendar.SECOND, 0)
        }
        Log.d("AlarmClockTaskManager", "上班时间:${dateFormat.format(calendar.time)}")
        //打卡时间=上班时间-提前多少分钟
        calendar.add(Calendar.MINUTE, -taskData.minute)

        //优化: 如果过了打卡时间，暂时不考虑迟到打卡的情况。那么下一个闹钟，应该是明天这个时间节点
        if (now.timeInMillis >= calendar.timeInMillis) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        Log.d("AlarmClockTaskManager", "上班打卡时间:${dateFormat.format(calendar.time)}")
        AlarmClockKit.instance.setAlarmClock(
            calendar,
            intentRequestCode = INTENT_REQUEST_CODE_START
        )
    }

    private fun stopStartWorkAlarmClock() {
        Log.d("AlarmClockTaskManager", "stopStartWorkAlarmClock")
        AlarmClockKit.instance.cancelAlarmClock(INTENT_REQUEST_CODE_START)
    }

    private fun appendEndWorkAlarmClock(taskData: TaskData) {
        Log.d("AlarmClockTaskManager", "appendEndWorkAlarmClock data:${taskData}")

        //现在的时间
        val now = Calendar.getInstance()

        //下班的时间
        val currentTimeMillis = System.currentTimeMillis()
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = currentTimeMillis
            set(Calendar.HOUR_OF_DAY, taskData.workTime.hour)
            set(Calendar.MINUTE, taskData.workTime.minute)
            set(Calendar.SECOND, 0)
        }
        Log.d("AlarmClockTaskManager", "下班时间:${dateFormat.format(calendar.time)}")
        //计算打卡时间=下班时间+延时多少分钟
        calendar.add(Calendar.MINUTE, taskData.minute)

        //优化: 如果过了打卡时间，暂时不考虑迟到打卡的情况。那么下一个闹钟，应该是明天这个时间节点
        if (now.timeInMillis >= calendar.timeInMillis) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        Log.d("AlarmClockTaskManager", "下班打卡时间:${dateFormat.format(calendar.time)}")
        AlarmClockKit.instance.setAlarmClock(
            calendar,
            intentRequestCode = INTENT_REQUEST_CODE_END
        )
    }

    private fun stopEndWorkAlarmClock() {
        Log.d("AlarmClockTaskManager", "stopEndWorkAlarmClock")
        AlarmClockKit.instance.cancelAlarmClock(INTENT_REQUEST_CODE_END)
    }


    companion object {
        val instance by lazy { AlarmClockTaskManager() }
        const val ALARM_CLOCK_TYPE = "alarmClockType"
        private const val INTENT_REQUEST_CODE_START = 1
        private const val INTENT_REQUEST_CODE_END = 2
    }
}

