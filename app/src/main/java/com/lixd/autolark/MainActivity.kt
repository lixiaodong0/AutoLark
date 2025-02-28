package com.lixd.autolark

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier

import com.lixd.autolark.ui.DrawerContent
import com.lixd.autolark.ui.MainScreen
import com.lixd.autolark.ui.MainViewModel
import com.lixd.autolark.ui.RunningScreen
import com.lixd.autolark.ui.theme.AutoLarkTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainViewModel.provider = viewModel
        enableEdgeToEdge()
        startService(Intent(this, MyForegroundService::class.java))
        setContent {
            AutoLarkTheme {

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val uiState by viewModel.mainUiState.collectAsState()

                ModalNavigationDrawer(drawerContent = {
                    DrawerContent(uiState, viewModel)
                }, gesturesEnabled = !uiState.isRunning, drawerState = drawerState) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Box(Modifier.padding(innerPadding)) {
                            if (!uiState.isRunning) {
                                MainScreen(viewModel = viewModel, onClickMenu = {
                                    scope.launch {
                                        if (drawerState.isClosed) {
                                            drawerState.open()
                                        } else {
                                            drawerState.close()
                                        }
                                    }
                                })
                            } else {
                                RunningScreen(viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}



