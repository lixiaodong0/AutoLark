package com.lixd.autolark.conf

data class WorkTime(val hour: Int, val minute: Int) {
    fun toHourStr(): String {
        return hour.toTimeStr()
    }

    fun toMinuteStr(): String {
        return minute.toTimeStr()
    }
}

fun Int.toTimeStr(): String {
    return if (this < 10) "0$this" else "$this"
}