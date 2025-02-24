package com.lixd.autolark.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp

@Composable
fun RunningButton(
    isRunning: Boolean = false,
    onRunning: () -> Unit,
    onStop: () -> Unit,
) {
    var innerRunningState by remember {
        mutableStateOf(isRunning)
    }

    var fullWidth by remember {
        mutableStateOf(0.dp)
    }
    val minWidth by remember {
        mutableStateOf(48.dp)
    }

    val widthDpAsState = if (!innerRunningState) {
        fullWidth
    } else {
        minWidth
    }

    var isStartAnimate by remember {
        mutableStateOf(false)
    }

    var showCircularProgress by remember {
        mutableStateOf(false)
    }

    val widthAnimateDpAsState by animateDpAsState(
        if (!innerRunningState) {
            fullWidth
        } else {
            minWidth
        },
        finishedListener = {
            if (innerRunningState) {
                showCircularProgress = true
                onRunning()
            } else {
                onStop()
            }
        }
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp)
            .onGloballyPositioned {
                fullWidth = it.size.width.dp
            }
    ) {
        Button(
            onClick = {
                isStartAnimate = true
                innerRunningState = !innerRunningState
                if (!innerRunningState) {
                    showCircularProgress = false
                }
            },
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .width(
                    if (isStartAnimate) {
                        widthAnimateDpAsState
                    } else {
                        widthDpAsState
                    }
                )
        ) {
            Text("运行")
        }
        if (showCircularProgress) {
            Box(modifier = Modifier.align(Alignment.Center), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(minWidth),
                    color = Color.White,
                    trackColor = MaterialTheme.colorScheme.primary,
                )
                Icon(rememberVectorPainter(Icons.Default.Close), null, tint = Color.White)
            }
        }
    }
}
