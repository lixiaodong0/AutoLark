package com.lixd.autolark.kit

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

object ToastKit {

    fun showToast(msg: String? = "", context: Context = ApplicationKit.appContext) {
        if (msg.isNullOrEmpty()) {
            return
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}