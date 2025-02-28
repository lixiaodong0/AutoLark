package com.lixd.autolark.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.system.exitProcess

@Composable
fun DrawerContent(uiState: MainUiState, viewModel: MainViewModel) {
    val activityContext = LocalContext.current
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .width(250.dp)
                .fillMaxHeight()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.size(12.dp))

            Text("软件介绍", fontSize = 20.sp, color = Color.Black)
            Spacer(Modifier.size(10.dp))
            Text(
                buildAnnotatedString {
                    //这是一款针对飞书极速打卡功能的自动化唤醒程序，配置上下班时间，即可在指定时间段唤醒飞书，从而实现自动打卡功能。
                    append("这是一款针对")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("飞书极速打卡功能的自动化唤醒程序")
                    }
                    append("，配置上下班时间，即可在指定时间段唤醒飞书，从而实现")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("自动打卡")
                    }
                    append("功能。")
                },
                fontSize = 12.sp,
                lineHeight = 24.sp,
                color = Color.Gray
            )
            HorizontalDivider(Modifier.padding(top = 16.dp))

            Spacer(Modifier.size(16.dp))
            Text(
                "实验性功能",
                fontSize = 20.sp,
                color = Color.Black,
            )
            Spacer(Modifier.size(20.dp))
            Box(Modifier.fillMaxWidth()) {
                Text(
                    "无痕模式",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Switch(uiState.checked, onCheckedChange = {
                    viewModel.handleIntent(
                        MainUiIntent.ModifyTracelessMode(
                            uiState.checked, activityContext
                        )
                    )
                }, modifier = Modifier.align(Alignment.CenterEnd))
            }
            Text(
                "开启后，APP处于后台情况下，最近任务栏将不会出现该APP。注意：每次修改此配置将会重启程序。",
                fontSize = 12.sp,
                color = Color.Gray,
            )

            Spacer(Modifier.weight(1f))
            Button(
                {
                    android.os.Process.killProcess(android.os.Process.myPid())
                    exitProcess(10)
                }, modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("退出APP")
            }
        }
    }
}