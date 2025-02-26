package com.lixd.autolark.kit

import android.content.Context
import com.lixd.autolark.conf.WorkTime
import com.lixd.autolark.task.TaskData
import org.json.JSONObject

class SimpleCacheKit private constructor() {

    private val sp by lazy {
        ApplicationKit.appContext.getSharedPreferences("simpleCache", Context.MODE_PRIVATE)
    }

    fun putBool(key: String, data: Boolean) {
        sp.edit().putBoolean(key, data).apply()
    }

    fun getBool(key: String): Boolean {
        return sp.getBoolean(key, false)
    }

    fun putString(key: String, data: String) {
        sp.edit().putString(key, data).apply()
    }

    fun getString(key: String): String {
        return sp.getString(key, "") ?: ""
    }

    fun putTaskData(key: String, taskData: TaskData) {
        val json = JSONObject().apply {
            put("hour", taskData.workTime.hour)
            put("minute", taskData.workTime.minute)
            put("punchInMinute", taskData.minute)
        }.toString()
        putString(key, json)
    }

    fun getTaskData(key: String): TaskData? {
        val json = getString(key)
        if (json.isEmpty()) {
            return null
        }
        try {
            val obj = JSONObject(json)
            return TaskData(
                WorkTime(
                    obj.getInt("hour"),
                    obj.getInt("minute")
                ),
                obj.getInt("punchInMinute")
            )
        } catch (_: Exception) {
        }
        return null
    }

    companion object {
        const val KEY_TRACELESS_MODE = "TracelessMode"
        const val KEY_START_WORK_TIME = "startWorkTime"
        const val KEY_END_WORK_TIME = "endWorkTime"
        val instance by lazy { SimpleCacheKit() }
    }
}