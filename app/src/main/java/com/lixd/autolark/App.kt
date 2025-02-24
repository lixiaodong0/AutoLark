package com.lixd.autolark

import android.app.Application
import com.lixd.autolark.kit.AlarmClockKit
import com.lixd.autolark.kit.ApplicationKit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AlarmClockKit.instance.init(this)
        ApplicationKit.appContext = this
    }
}