package com.lixd.autolark.kit

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import com.lixd.autolark.conf.LARK_PACKAGE_NAME

object ApplicationKit {
    lateinit var appContext: Context

    fun restartApp(context: Context = appContext, packageName: String = LARK_PACKAGE_NAME) {
        killApp(context, packageName)
        launchApp(context, packageName)
    }

    fun launchApp(context: Context = appContext, packageName: String = LARK_PACKAGE_NAME) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun killApp(context: Context = appContext, packageName: String = LARK_PACKAGE_NAME) {
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses(packageName)
    }
}