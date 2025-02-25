package com.lixd.autolark

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.lixd.autolark.task.WakeAppTaskManager
import com.lixd.autolark.ui.MainScreen
import com.lixd.autolark.ui.MainViewModel
import com.lixd.autolark.ui.theme.AutoLarkTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainViewModel.provider = viewModel
        enableEdgeToEdge()
        startService(Intent(this, MyForegroundService::class.java))
        setContent {
            AutoLarkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(Modifier.padding(innerPadding), viewModel)
                }
            }
        }
    }
}



