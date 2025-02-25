package com.lixd.autolark.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.buildSpannedString
import com.lixd.autolark.kit.ToastKit
import com.lixd.autolark.ui.theme.AutoLarkTheme

@Composable
fun DescriptionContainer(
    onClickDownload: () -> Unit = {},
    onClickConfig: () -> Unit = {},
    onClickFloatPermission: () -> Unit = {},
    onClickBackgroundPermission: () -> Unit = {},
) {
    val desc1 = buildAnnotatedString {
        //1.下载飞书，飞书配置极速打卡功能。
        append("1.")
        withStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            pushStringAnnotation("url", "downloadLark")
            append("下载飞书")
            pop()
        }
        append("，飞书")
        withStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            pushStringAnnotation("url", "configLark")
            append("配置极速打卡")
            pop()
        }
        append("功能。")
    }

    val desc2 = buildAnnotatedString {
        //2.为了保障软件正常工作，软件需要开启《悬浮窗》权限，《后台弹出界面》权限。用来APP置为后台的情况下，用来拉起APP。
        append("2.为了保障软件正常工作，软件需要")
        withStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            pushStringAnnotation("url", "openFloatPermission")
            append("开启《悬浮窗》权限")
            pop()
        }
        append("。部分设备机型还需额外")
        withStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            pushStringAnnotation("url", "openBackgroundPermission")
            append("开启《后台弹出界面》权限")
            pop()
        }
        append("。用来APP置为后台的情况下，用来拉起APP。")
    }


    Column(Modifier.padding(16.dp)) {
        Text("软件介绍：", fontSize = 16.sp, color = Color.Black)
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
        Spacer(Modifier.size(10.dp))
        Text("使用说明：", fontSize = 16.sp, color = Color.Black)
        Spacer(Modifier.size(4.dp))
        ClickableText(
            desc1,
            style = TextStyle(
                lineHeight = 24.sp,
                fontSize = 12.sp,
                color = Color.Gray,
            ),
            onClick = { offset ->
                desc1.getStringAnnotations(tag = "url", start = offset, end = offset)
                    .firstOrNull()?.let {
                        if (it.item == "downloadLark") {
                            //下载
                            ToastKit.showToast("下载")
                        } else if (it.item == "configLark") {
                            //配置
                            ToastKit.showToast("配置")
                        }
                    }
            },
        )
        Spacer(Modifier.size(4.dp))
        ClickableText(
            desc2,
            style = TextStyle(
                lineHeight = 24.sp,
                fontSize = 12.sp,
                color = Color.Gray
            ),
            onClick = { offset ->
                desc2.getStringAnnotations(tag = "url", start = offset, end = offset)
                    .firstOrNull()?.let {
                        if (it.item == "openFloatPermission") {
                            //下载
                            ToastKit.showToast("打开悬浮权限")
                        } else if (it.item == "openBackgroundPermission") {
                            //配置
                            ToastKit.showToast("打开后台弹出权限")
                        }
                    }
            },
        )
        Spacer(Modifier.size(4.dp))
        Text(
            "3.配置上下班时间，打卡时间，点击运行，自动运行打卡任务。",
            fontSize = 12.sp,
            lineHeight = 24.sp,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DescriptionContainerPreview() {
    AutoLarkTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            DescriptionContainer()
        }
    }
}