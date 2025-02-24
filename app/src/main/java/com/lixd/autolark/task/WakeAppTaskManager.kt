package com.lixd.autolark.task

import android.util.Log
import com.lixd.autolark.App
import com.lixd.autolark.kit.ApplicationKit
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WakeAppTaskManager private constructor() {
    private var runningTask: Job? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun startTask() {
        stopTask()
        runningTask = GlobalScope.launch(Dispatchers.IO) {
            Log.d("WakeAppTaskManager", "------Wake App Task Start----")
            for (i in 0 until DEFAULT_MAX_WAKE_COUNT) {
                Log.d("WakeAppTaskManager", "${i + 1} kill app wait 3s launch app")
                //1.杀死飞书APP
                withContext(Dispatchers.Main) {
                    ApplicationKit.killApp()
                }
                //2.等待个3s,确保进程被杀死
                delay(3000)
                Log.d("WakeAppTaskManager", "${i + 1} launch app wait 57s")
                //3.唤醒飞书APP
                withContext(Dispatchers.Main) {
                    ApplicationKit.launchApp()
                }
                //4.启动飞书后，大概等待一分钟，等待飞书自动触发急速打卡
                delay(57000)
                Log.d("WakeAppTaskManager", "${i + 1} launch my app wait 3s")
                //5.为了确保下一次能够正常执行，需要把我们自己APP进程拉到前台，因为killApp只能杀死后台进程
                ApplicationKit.launchApp(packageName = ApplicationKit.appContext.packageName)
                delay(3000)
            }
            Log.d("WakeAppTaskManager", "------Wake App Task End----")
        }
    }

    fun stopTask() {
        Log.d("WakeAppTaskManager", "------Wake App Task cancel----")
        runningTask?.cancel()
    }

    companion object {
        val instance by lazy { WakeAppTaskManager() }
        private const val DEFAULT_MAX_WAKE_COUNT = 3
    }
}
