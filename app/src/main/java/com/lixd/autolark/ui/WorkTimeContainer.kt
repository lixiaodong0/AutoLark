package com.lixd.autolark.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lixd.autolark.conf.WorkTime
import com.lixd.autolark.conf.toTimeStr
import com.lixd.autolark.ui.theme.SmallNumberSize
import com.lixd.autolark.ui.theme.SmallNumberTextSize

@Composable
fun WorkTimeContainer(
    title: String,
    subtitle: String = "打卡时间",
    workTime: WorkTime,
    minute: Int,
    punchInTime: String,
    onClickWorkTime: (WorkTime) -> Unit,
    onClickMinute: (Int) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(Modifier.weight(0.6f)) {
                Column {
                    Text(title)
                    Spacer(Modifier.size(10.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                onClickWorkTime(workTime)
                            },
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TimeContent(workTime.toHourStr())
                        Text(
                            ":",
                            fontSize = SmallNumberTextSize,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        TimeContent(workTime.toMinuteStr())
                        Spacer(Modifier.size(10.dp))
                        Icon(
                            rememberVectorPainter(Icons.Default.KeyboardArrowRight),
                            null,
                        )
                    }
                }
            }
            Box(Modifier.weight(0.4f)) {
                Column(Modifier.align(Alignment.Center)) {
                    Text(subtitle)
                    Spacer(Modifier.size(10.dp))
                    Row(
                        Modifier
                            .clickable {
                                onClickMinute(minute)
                            },
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MinuteContent(minute)
                        Spacer(Modifier.size(4.dp))
                        Text("/分钟", fontSize = 14.sp, color = Color.Gray)
                        Spacer(Modifier.size(10.dp))
                        Icon(
                            rememberVectorPainter(Icons.Default.KeyboardArrowRight),
                            null,
                        )
                    }
                }
            }
        }
        Spacer(Modifier.size(8.dp))
        Text("预计：${punchInTime} 开始打卡", fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
private fun TimeContent(time: String) {
    Surface(
        modifier = SmallNumberSize,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                fontSize = SmallNumberTextSize,
                text = time,
            )
        }
    }
}

@Composable
private fun MinuteContent(minute: Int) {
    Surface(
        modifier = SmallNumberSize,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                fontSize = SmallNumberTextSize,
                text = minute.toTimeStr(),
            )
        }
    }
}