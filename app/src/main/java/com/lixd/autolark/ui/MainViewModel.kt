package com.lixd.autolark.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lixd.autolark.conf.WorkTime
import com.lixd.autolark.kit.ApplicationKit
import com.lixd.autolark.kit.DateKit
import com.lixd.autolark.kit.ToastKit
import com.lixd.autolark.task.AlarmClockTaskManager
import com.lixd.autolark.task.TaskData
import com.lixd.autolark.task.WakeAppTaskManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState = _mainUiState.asStateFlow()

    init {
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
                _mainUiState.update {
                    it.copy(
                        isRunning = true
                    )
                }
                val startTaskData = _mainUiState.value.toStartTaskData()
                val endTaskData = _mainUiState.value.toEndTaskData()
                AlarmClockTaskManager.instance.start(startTaskData, endTaskData)
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
    val isRunning: Boolean = false,
) {
    fun toStartTaskData(): TaskData {
        return TaskData(startTime, startMinute)
    }

    fun toEndTaskData(): TaskData {
        return TaskData(endTime, endMinute)
    }
}