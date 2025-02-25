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

    fun toHome(context: Context = appContext): Boolean {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        try {
            context.startActivity(intent)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun launchApp(context: Context = appContext, packageName: String = LARK_PACKAGE_NAME): Boolean {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                context.startActivity(intent)
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    fun killApp(context: Context = appContext, packageName: String = LARK_PACKAGE_NAME) {
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses(packageName)
    }
}