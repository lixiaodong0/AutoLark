package com.lixd.autolark

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.lixd.autolark.kit.ApplicationKit
import com.lixd.autolark.kit.SimpleCacheKit
import com.lixd.autolark.kit.SimpleCacheKit.Companion.KEY_TRACELESS_MODE

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isTracelessMode = SimpleCacheKit.instance.getBool(KEY_TRACELESS_MODE)
        //优先关闭，只保留MainActivity在根栈，否则动态修改excludeFromRecents配置无效。
        finish()
        ApplicationKit.launchTracelessModeActivity(
            isTracelessMode,
            MainActivity::class.java,
            this
        )
    }
}