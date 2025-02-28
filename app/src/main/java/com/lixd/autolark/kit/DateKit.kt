package com.lixd.autolark.kit

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateKit {
    @SuppressLint("ConstantLocale")
    private val dateFormat: SimpleDateFormat =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val timeFormat: SimpleDateFormat =
        SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    fun calculatePunchInTime(
        hour: Int, minute: Int,
        punchInMinute: Int,
    ): Calendar {
        //现在的时间
        val now = Calendar.getInstance()
        //上班的时间
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = now.timeInMillis
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }
        //打卡时间=上班时间-提前多少分钟
        calendar.add(Calendar.MINUTE, punchInMinute)
        //优化: 如果过了打卡时间，暂时不考虑迟到打卡的情况。那么下一个闹钟，应该是明天这个时间节点
        if (now.timeInMillis >= calendar.timeInMillis) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar
    }


    fun calculatePunchInTime2Str(
        hour: Int, minute: Int,
        punchInMinute: Int,
    ): String {
        val now = Calendar.getInstance()
        val next = calculatePunchInTime(hour, minute, punchInMinute)
        return if (isSameDay(now, next)) {
            "今天 ${timeFormat.format(next.time)}"
        } else {
            "明天 ${timeFormat.format(next.time)}"
        }
    }


    fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }
}