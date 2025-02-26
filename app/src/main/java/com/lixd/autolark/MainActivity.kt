package com.lixd.autolark

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.lixd.autolark.kit.ApplicationKit
import com.lixd.autolark.kit.SimpleCacheKit
import com.lixd.autolark.kit.SimpleCacheKit.Companion.KEY_TRACELESS_MODE
import com.lixd.autolark.task.WakeAppTaskManager
import com.lixd.autolark.ui.MainScreen
import com.lixd.autolark.ui.MainUiIntent
import com.lixd.autolark.ui.MainViewModel
import com.lixd.autolark.ui.theme.AutoLarkTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private val activityContext by lazy { this }

    fun hasFlag(intent: Intent, flag: Int): Boolean {
        return (intent.flags and flag) == flag
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainViewModel.provider = viewModel
        val isTracelessMode = SimpleCacheKit.instance.getBool(KEY_TRACELESS_MODE)

        Log.d("MainActivity", "isTracelessMode=${isTracelessMode},intent.flags=${intent.flags}")
        if (isTracelessMode && !hasFlag(intent, Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)) {
            Log.d("MainActivity", "启动发现无痕模式配置，开启无痕模式")
            finish()
            ApplicationKit.launchTracelessModeActivity(
                true,
                MainActivity::class.java,
                this
            )
            return
        }

        enableEdgeToEdge()
        startService(Intent(this, MyForegroundService::class.java))
        setContent {
            AutoLarkTheme {

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val uiState by viewModel.mainUiState.collectAsState()

                ModalNavigationDrawer(drawerContent = {
                    ModalDrawerSheet {
                        Column(
                            modifier = Modifier
                                .width(250.dp)
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
                        }
                    }
                }, drawerState = drawerState) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        MainScreen(Modifier.padding(innerPadding), viewModel, onClickMenu = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        })
                    }
                }
            }
        }
    }
}



