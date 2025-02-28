package com.lixd.autolark.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

fun Long.formatDuration(): String {
    val days = this / 86400
    val hours = (this % 86400) / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    return when {
        days > 0 -> "${days}天 " +
                "${hours.toString().padStart(2, '0')}:" +
                "${minutes.toString().padStart(2, '0')}:" +
                seconds.toString().padStart(2, '0')

        else -> "${hours.toString().padStart(2, '0')}:" +
                "${minutes.toString().padStart(2, '0')}:" +
                seconds.toString().padStart(2, '0')
    }
}

@Composable
fun RunningScreen(viewModel: MainViewModel) {
    val uiState by viewModel.mainUiState.collectAsState()
    var time by remember {
        mutableLongStateOf(0L)
    }
    LaunchedEffect(Unit) {
        launch(Dispatchers.IO) {
            while (isActive) {
                time = (System.currentTimeMillis() - uiState.startRunningTime) / 1000
                delay(1000)
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(time.formatDuration(), fontSize = 16.sp, color = Color.Black)
            Spacer(Modifier.size(10.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable {
                        viewModel.handleIntent(MainUiIntent.Stop)
                    }) {
                CircularProgressIndicator(
                    modifier = Modifier.size(68.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    trackColor = MaterialTheme.colorScheme.primary,
                )
                Column {
                    Text("停止", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}