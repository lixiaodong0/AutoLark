package com.lixd.autolark.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lixd.autolark.ui.theme.AutoLarkTheme

@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val uiState by viewModel.mainUiState.collectAsState()

    var showTimePickerDialog by remember {
        mutableStateOf<TimePickerDialogState>(TimePickerDialogState.Dismiss)
    }
    var showMinutePickerDialog by remember {
        mutableStateOf<MinutePickerDialogState>(MinutePickerDialogState.Dismiss)
    }

    Box(modifier = modifier) {
        Column {
            DescriptionContainer(onClickDownload = {

            }, onClickConfig = {

            }, onClickFloatPermission = {

            }, onClickBackgroundPermission = {

            })
            HorizontalDivider(Modifier.padding(horizontal = 16.dp), thickness = 1.dp)

            WorkTimeContainer(uiState.startTime, uiState.endTime, onClickStartTime = {
                showTimePickerDialog =
                    TimePickerDialogState.Show(TimePickerType.StartTime, uiState.startTime)
            }, onClickEndTime = {
                showTimePickerDialog =
                    TimePickerDialogState.Show(TimePickerType.EndTime, uiState.endTime)
            })
            HorizontalDivider(Modifier.padding(horizontal = 16.dp), thickness = 2.dp)
            OpenCardTimeContainer(uiState.startMinute, uiState.endMinute, onClickStartMinute = {
                showMinutePickerDialog =
                    MinutePickerDialogState.Show(MinutePickerType.StartMinute(it))
            }, onClickEndMinute = {
                showMinutePickerDialog =
                    MinutePickerDialogState.Show(MinutePickerType.EndMinute(it))
            })
            Spacer(Modifier.size(30.dp))
            RunningButton(uiState.isRunning, onRunning = {
                viewModel.handleIntent(MainUiIntent.Start)
            }, onStop = {
                viewModel.handleIntent(MainUiIntent.Stop)
            })
        }
    }

    if (showTimePickerDialog is TimePickerDialogState.Show) {
        val initialHour = (showTimePickerDialog as TimePickerDialogState.Show).workTime.hour
        val initialMinute = (showTimePickerDialog as TimePickerDialogState.Show).workTime.minute
        TimePickerDialog(
            initialHour = initialHour,
            initialMinute = initialMinute,
            onDismiss = {
                showTimePickerDialog = TimePickerDialogState.Dismiss
            },
            onConfirm = { hour, minute ->
                Log.d("TimePickerDialog", "hour:${hour},minute:${minute}")
                val show = showTimePickerDialog as? TimePickerDialogState.Show
                show?.let {
                    when (it.timePickerType) {
                        TimePickerType.StartTime -> {
                            viewModel.handleIntent(MainUiIntent.UpdateStartTime(hour, minute))
                        }

                        TimePickerType.EndTime -> {
                            viewModel.handleIntent(MainUiIntent.UpdateEndTime(hour, minute))
                        }
                    }
                }
                showTimePickerDialog = TimePickerDialogState.Dismiss
            }
        )
    }

    if (showMinutePickerDialog is MinutePickerDialogState.Show) {
        MinutePickerDialog(
            (showMinutePickerDialog as MinutePickerDialogState.Show).minutePickerType,
            onDismiss = {
                showMinutePickerDialog = MinutePickerDialogState.Dismiss
            },
            onConfirm = { minute ->
                Log.d("MinutePickerDialog", "minute:${minute}")
                val show = showMinutePickerDialog as? MinutePickerDialogState.Show
                show?.let {
                    when (it.minutePickerType) {
                        is MinutePickerType.StartMinute -> {
                            viewModel.handleIntent(MainUiIntent.UpdateStartMinute(minute))
                        }

                        is MinutePickerType.EndMinute -> {
                            viewModel.handleIntent(MainUiIntent.UpdateEndMinute(minute))
                        }
                    }
                }
                showMinutePickerDialog = MinutePickerDialogState.Dismiss
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AutoLarkTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            MainScreen()
        }
    }
}