package com.lixd.autolark.kit

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import com.lixd.autolark.MainActivity
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
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(intent)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun toSettings(context: Context = appContext): Boolean {
        val intent = Intent(Settings.ACTION_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
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

    fun isAppInstalled(
        context: Context = appContext,
        packageName: String = LARK_PACKAGE_NAME
    ): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun toMarket(
        context: Context = appContext,
        packageName: String = LARK_PACKAGE_NAME
    ) {
        val uri = Uri.parse("market://details?id=$packageName")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun checkOverlayPermissionSettings() = Settings.canDrawOverlays(appContext)

    fun openOverlayPermissionSettings() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + appContext.packageName)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        appContext.startActivity(intent)
    }

    fun openAppPermissionSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + appContext.packageName)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        appContext.startActivity(intent)
    }

    fun launchTracelessModeActivity(
        isTracelessMode: Boolean = true,
        clazz: Class<*>,
        activityContext: Context
    ) {
        val intent = Intent(activityContext, clazz).apply {
            if (isTracelessMode) {
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            }
        }
        activityContext.startActivity(intent)
    }
}
