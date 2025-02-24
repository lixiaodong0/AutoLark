package com.lixd.autolark.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lixd.autolark.conf.WorkTime
import java.util.Calendar

sealed class TimePickerType {
    data object StartTime : TimePickerType()
    data object EndTime : TimePickerType()
}

sealed class TimePickerDialogState {
    data class Show(val timePickerType: TimePickerType, val workTime: WorkTime) :
        TimePickerDialogState()

    data object Dismiss : TimePickerDialogState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialHour: Int? = null,
    initialMinute: Int? = null,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour ?: currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = initialMinute ?: currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )
    Dialog(onDismissRequest = {
        onDismiss()
    }) {
        TimePickerExample(timePickerState, onConfirm, onDismiss)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerExample(
    timePickerState: TimePickerState,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
) {
    Surface(Modifier.fillMaxWidth(0.9f), shape = RoundedCornerShape(16.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimePicker(state = timePickerState)
            Row {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("取消")
                }
                Spacer(Modifier.size(20.dp))
                Button(
                    onClick = {
                        onConfirm(timePickerState.hour, timePickerState.minute)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("确定")
                }
            }
        }
    }
}