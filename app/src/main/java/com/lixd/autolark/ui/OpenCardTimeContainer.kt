package com.lixd.autolark.ui

import androidx.compose.foundation.clickable
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
import com.lixd.autolark.conf.toTimeStr
import com.lixd.autolark.ui.theme.SmallNumberSize
import com.lixd.autolark.ui.theme.SmallNumberTextSize

@Composable
fun OpenCardTimeContainer(
    startMinute: Int,
    endMinute: Int,
    onClickStartMinute: (Int) -> Unit,
    onClickEndMinute: (Int) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClickStartMinute(startMinute)
                }, verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Text("打上班卡，提前")
                Spacer(Modifier.size(8.dp))
                MinuteContent(startMinute)
                Spacer(Modifier.size(8.dp))
                Text("分钟")
            }
            Icon(
                rememberVectorPainter(Icons.Default.KeyboardArrowRight),
                null,
            )
        }
        Spacer(Modifier.size(16.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClickEndMinute(endMinute)
                }, verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Text("打下班卡，延后")
                Spacer(Modifier.size(8.dp))
                MinuteContent(endMinute)
                Spacer(Modifier.size(8.dp))
                Text("分钟")
            }
            Icon(
                rememberVectorPainter(Icons.Default.KeyboardArrowRight),
                null,
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