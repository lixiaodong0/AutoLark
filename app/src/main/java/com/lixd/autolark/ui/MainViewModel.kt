package com.lixd.autolark.ui

import androidx.lifecycle.ViewModel
import com.lixd.autolark.conf.WorkTime
import com.lixd.autolark.task.AlarmClockTaskManager
import com.lixd.autolark.task.TaskData
import com.lixd.autolark.task.WakeAppTaskManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState = _mainUiState.asStateFlow()

    fun handleIntent(intent: MainUiIntent) {
        when (intent) {
            is MainUiIntent.UpdateStartTime -> {
                _mainUiState.update {
                    it.copy(
                        startTime = it.startTime.copy(
                            hour = intent.hour,
                            minute = intent.minute
                        )
                    )
                }
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
            }

            is MainUiIntent.UpdateStartMinute -> {
                _mainUiState.update {
                    it.copy(
                        startMinute = intent.minute
                    )
                }
            }

            is MainUiIntent.UpdateEndMinute -> {
                _mainUiState.update {
                    it.copy(
                        endMinute = intent.minute
                    )
                }
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
        }
    }
}


sealed class MainUiIntent {
    data class UpdateStartTime(val hour: Int, val minute: Int) : MainUiIntent()
    data class UpdateEndTime(val hour: Int, val minute: Int) : MainUiIntent()
    data class UpdateStartMinute(val minute: Int) : MainUiIntent()
    data class UpdateEndMinute(val minute: Int) : MainUiIntent()
    data object Start : MainUiIntent()
    data object Stop : MainUiIntent()
}

data class MainUiState(
    val startTime: WorkTime = WorkTime(10, 0),
    val endTime: WorkTime = WorkTime(19, 30),
    val startMinute: Int = 20,
    val endMinute: Int = 1,
    val isRunning: Boolean = false,
) {
    fun toStartTaskData(): TaskData {
        return TaskData(startTime, startMinute)
    }

    fun toEndTaskData(): TaskData {
        return TaskData(endTime, endMinute)
    }
}