package com.lixd.autolark.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.lixd.autolark.conf.WorkTime

sealed class MinutePickerType(open val defaultMinute: Int) {
    data class StartMinute(override val defaultMinute: Int) : MinutePickerType(defaultMinute)
    data class EndMinute(override val defaultMinute: Int) : MinutePickerType(defaultMinute)
}

sealed class MinutePickerDialogState {
    data class Show(val minutePickerType: MinutePickerType) :
        MinutePickerDialogState()

    data object Dismiss : MinutePickerDialogState()
}

@Composable
fun MinutePickerDialog(
    minutePickerType: MinutePickerType,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = {
        onDismiss()
    }) {
        MinutePickerExample(minutePickerType, minutePickerType.defaultMinute, onConfirm, onDismiss)
    }
}

@Composable
private fun MinutePickerExample(
    minutePickerType: MinutePickerType,
    defaultMinute: Int = -1,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val list by remember {
        if (minutePickerType is MinutePickerType.StartMinute) {
            mutableStateOf(1..30)
        } else {
            mutableStateOf(0..30)
        }
    }
    val subtitle by remember {
        if (minutePickerType is MinutePickerType.StartMinute) {
            mutableStateOf("提前")
        } else {
            mutableStateOf("延后")
        }
    }
    var current by remember {
        mutableIntStateOf(defaultMinute)
    }
    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
        Box(Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val title = if (current != -1) {
                    "${subtitle}${current}分钟打卡"
                } else {
                    "请选择${subtitle}分钟打卡"
                }
                Text(
                    title,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.size(16.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(list.count()) {
                        val backgroundColor = if (it == current) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceContainer
                        }
                        val textColor = if (it == current) {
                            Color.White
                        } else {
                            Color.Black
                        }
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .background(
                                    backgroundColor,
                                    RoundedCornerShape(4.dp)
                                )
                                .clickable {
                                    current = it
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("$it", fontSize = 16.sp, color = textColor)
                        }
                    }
                }
                Spacer(Modifier.size(16.dp))
                Row(Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            onDismiss()
                        }, modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("取消", fontSize = 18.sp)
                    }
                    Spacer(Modifier.size(20.dp))
                    Button(
                        onClick = {
                            onConfirm(current)
                        }, modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("确定", fontSize = 18.sp)
                    }

                }
            }
        }
    }
}