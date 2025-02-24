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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lixd.autolark.conf.WorkTime
import com.lixd.autolark.ui.theme.SmallNumberSize
import com.lixd.autolark.ui.theme.SmallNumberTextSize

@Composable
fun WorkTimeContainer(
    startTime: WorkTime,
    endTime: WorkTime,
    onClickStartTime: () -> Unit,
    onClickEndTime: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("上班时间")
        Spacer(Modifier.size(10.dp))
        Box(modifier = Modifier
            .clickable {
                onClickStartTime()
            }
            .padding(start = 16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeContent(startTime.toHourStr())
                Text(
                    ":",
                    fontSize = SmallNumberTextSize,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                TimeContent(startTime.toMinuteStr())
            }
            Icon(
                rememberVectorPainter(Icons.Default.KeyboardArrowRight),
                null,
                modifier = Modifier.align(
                    Alignment.CenterEnd
                )
            )
        }
        Spacer(Modifier.size(16.dp))
        Text("下班时间")
        Spacer(Modifier.size(10.dp))
        Box(modifier = Modifier
            .clickable {
                onClickEndTime()
            }
            .padding(start = 16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeContent(endTime.toHourStr())
                Text(
                    ":",
                    fontSize = SmallNumberTextSize,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                TimeContent(endTime.toMinuteStr())
            }
            Icon(
                rememberVectorPainter(Icons.Default.KeyboardArrowRight),
                null,
                modifier = Modifier.align(
                    Alignment.CenterEnd
                )
            )
        }
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