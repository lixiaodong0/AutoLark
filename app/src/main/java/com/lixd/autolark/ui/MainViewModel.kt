package com.lixd.autolark.ui

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lixd.autolark.MainActivity
import com.lixd.autolark.conf.WorkTime
import com.lixd.autolark.kit.ApplicationKit
import com.lixd.autolark.kit.DateKit
import com.lixd.autolark.kit.SimpleCacheKit
import com.lixd.autolark.kit.SimpleCacheKit.Companion.KEY_END_WORK_TIME
import com.lixd.autolark.kit.SimpleCacheKit.Companion.KEY_START_WORK_TIME
import com.lixd.autolark.kit.SimpleCacheKit.Companion.KEY_TRACELESS_MODE
import com.lixd.autolark.kit.ToastKit
import com.lixd.autolark.task.AlarmClockTaskManager
import com.lixd.autolark.task.TaskData
import com.lixd.autolark.task.WakeAppTaskManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState = _mainUiState.asStateFlow()

    init {
        val checked = SimpleCacheKit.instance.getBool(KEY_TRACELESS_MODE)
        val start = SimpleCacheKit.instance.getTaskData(KEY_START_WORK_TIME)
        val end = SimpleCacheKit.instance.getTaskData(KEY_END_WORK_TIME)
        _mainUiState.update {
            it.copy(
                checked = checked,
                startTime = start?.workTime ?: WorkTime(10, 0),
                startMinute = start?.minute ?: 20,
                endTime = end?.workTime ?: WorkTime(19, 30),
                endMinute = end?.minute ?: 1,
            )
        }
        calculatePunchInTime()
    }

    fun handleIntent(intent: MainUiIntent) {
        when (intent) {
            is MainUiIntent.CalculatePunchInTime -> {
                calculatePunchInTime()
            }

            is MainUiIntent.UpdateStartTime -> {
                _mainUiState.update {
                    it.copy(
                        startTime = it.startTime.copy(
                            hour = intent.hour,
                            minute = intent.minute
                        )
                    )
                }
                calculatePunchInTime()
            }

            is MainUiIntent.UpdateEndTime -> {
                _mainUiState.update {
                    it.copy(
                        endTime = it.endTime.copy(
                            hour = intent.hour,
                            minute = intent.minute
                        )
                    )
                }
                calculatePunchInTime()
            }

            is MainUiIntent.UpdateStartMinute -> {
                _mainUiState.update {
                    it.copy(
                        startMinute = intent.minute
                    )
                }
                calculatePunchInTime()
            }

            is MainUiIntent.UpdateEndMinute -> {
                _mainUiState.update {
                    it.copy(
                        endMinute = intent.minute
                    )
                }
                calculatePunchInTime()
            }


            MainUiIntent.Start -> {
                if (!ApplicationKit.isAppInstalled()) {
                    ToastKit.showToast("飞书未安装，请阅读使用说明")
                    return
                }
                if (!ApplicationKit.checkOverlayPermissionSettings()) {
                    ToastKit.showToast("悬浮窗权限未开启，请阅读使用说明")
                    return
                }
                val startTaskData = _mainUiState.value.toStartTaskData()
                val endTaskData = _mainUiState.value.toEndTaskData()
                AlarmClockTaskManager.instance.start(startTaskData, endTaskData)
                viewModelScope.launch {
                    SimpleCacheKit.instance.putTaskData(KEY_START_WORK_TIME, startTaskData)
                    SimpleCacheKit.instance.putTaskData(KEY_END_WORK_TIME, endTaskData)
                }
                _mainUiState.update {
                    it.copy(
                        isRunning = true
                    )
                }
                viewModelScope.launch {
                    delay(500)
                    ApplicationKit.toHome()
                }
            }

            MainUiIntent.Stop -> {
                _mainUiState.update {
                    it.copy(
                        isRunning = false
                    )
                }
                WakeAppTaskManager.instance.stopTask()
                AlarmClockTaskManager.instance.stop()
            }

            MainUiIntent.Config -> {
                if (ApplicationKit.isAppInstalled()) {
                    ToastKit.showToast("请检查飞书配置极速打卡已开启")
                    ApplicationKit.launchApp()
                } else {
                    ApplicationKit.toMarket()
                    ToastKit.showToast("请先下载并安装飞书")
                }
            }

            MainUiIntent.Download -> {
                if (ApplicationKit.isAppInstalled()) {
                    ToastKit.showToast("飞书已安装")
                } else {
                    ApplicationKit.toMarket()
                    ToastKit.showToast("请先下载并安装飞书")
                }
            }

            MainUiIntent.OpenBackgroundPermission -> {
                ApplicationKit.openAppPermissionSettings()
            }

            MainUiIntent.OpenFloatPermission -> {
                ApplicationKit.openOverlayPermissionSettings()
            }

            is MainUiIntent.ModifyTracelessMode -> {
                if (_mainUiState.value.isRunning) {
                    ToastKit.showToast("任务运行中，请先停止任务")
                    return
                }
                viewModelScope.launch {
                    var isTracelessMode = false
                    if (intent.checked) {
                        SimpleCacheKit.instance.putBool(KEY_TRACELESS_MODE, false)
                        isTracelessMode = false
                    } else {
                        SimpleCacheKit.instance.putBool(KEY_TRACELESS_MODE, true)
                        isTracelessMode = true
                    }
                    ApplicationKit.setExcludeFromRecents(isTracelessMode)
                    _mainUiState.update {
                        it.copy(
                            checked = isTracelessMode
                        )
                    }
//                    delay(200)
//                    withContext(Dispatchers.Main) {
//                        val activity = intent.activityContext as? Activity
//                        activity?.let {
//                            it.finish()
//                            ApplicationKit.launchTracelessModeActivity(
//                                isTracelessMode,
//                                MainActivity::class.java,
//                                it
//                            )
//                        }
//                    }
                }
            }
        }

    }

    private fun calculatePunchInTime() {
        viewModelScope.launch {
            val startTime = _mainUiState.value.startTime
            val startMinute = _mainUiState.value.startMinute
            val startPunchInTimeStr =
                DateKit.calculatePunchInTime2Str(startTime.hour, startTime.minute, -startMinute)


            val endTime = _mainUiState.value.endTime
            val endMinute = _mainUiState.value.endMinute
            val endPunchInTimeStr =
                DateKit.calculatePunchInTime2Str(endTime.hour, endTime.minute, endMinute)

            _mainUiState.update {
                it.copy(
                    outStartTimeStr = startPunchInTimeStr,
                    outEndTimeStr = endPunchInTimeStr
                )
            }
        }
    }

    companion object {
        lateinit var provider: MainViewModel

        fun recalculatePunchInTime() {
            if (::provider.isInitialized) {
                provider.calculatePunchInTime()
            }
        }


        fun restartStartWorkAlarmClock(): Boolean {
            if (::provider.isInitialized) {
                val taskData = provider.mainUiState.value.toStartTaskData()
                AlarmClockTaskManager.instance.appendStartWorkAlarmClock(taskData)
                return true
            }
            return false
        }

        fun restartEndWorkAlarmClock(): Boolean {
            if (::provider.isInitialized) {
                val taskData = provider.mainUiState.value.toEndTaskData()
                AlarmClockTaskManager.instance.appendEndWorkAlarmClock(taskData)
                return true
            }
            return false
        }
    }
}


sealed class MainUiIntent {
    data class UpdateStartTime(val hour: Int, val minute: Int) : MainUiIntent()
    data class UpdateEndTime(val hour: Int, val minute: Int) : MainUiIntent()
    data class UpdateStartMinute(val minute: Int) : MainUiIntent()
    data class UpdateEndMinute(val minute: Int) : MainUiIntent()
    data object CalculatePunchInTime : MainUiIntent()
    data object Download : MainUiIntent()
    data object Config : MainUiIntent()
    data object OpenFloatPermission : MainUiIntent()
    data object OpenBackgroundPermission : MainUiIntent()
    data class ModifyTracelessMode(val checked: Boolean, val activityContext: Context) :
        MainUiIntent()

    data object Start : MainUiIntent()
    data object Stop : MainUiIntent()
}

data class MainUiState(
    val startTime: WorkTime = WorkTime(10, 0),
    val endTime: WorkTime = WorkTime(19, 30),
    val startMinute: Int = 20,
    val endMinute: Int = 1,
    val outStartTimeStr: String = "",
    val outEndTimeStr: String = "",
    val checked: Boolean = false,
    val isRunning: Boolean = false,
) {
    fun toStartTaskData(): TaskData {
        return TaskData(startTime, startMinute)
    }

    fun toEndTaskData(): TaskData {
        return TaskData(endTime, endMinute)
    }
}