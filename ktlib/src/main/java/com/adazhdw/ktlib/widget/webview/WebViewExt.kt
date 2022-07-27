package com.adazhdw.ktlib.widget.webview

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.webkit.WebSettings
import android.webkit.WebView


@SuppressLint("SetJavaScriptEnabled")
fun WebView.initSetting() {
    //5.0以上开启混合模式加载
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
    }
    settings.loadWithOverviewMode = true
    settings.useWideViewPort = true
    //允许js代码
    settings.javaScriptEnabled = true
    //允许SessionStorage/LocalStorage存储
    settings.domStorageEnabled = true
    //禁用放缩
    settings.builtInZoomControls = false
    settings.displayZoomControls = false
    //禁用文字缩放
    settings.textZoom = 100
    //10M缓存，api 18后，系统自动管理。
//    settings.setAppCacheMaxSize(10 * 1024 * 1024)
//    //允许缓存，设置缓存位置
//    settings.setAppCacheEnabled(true)
//    settings.setAppCachePath(context.getDir(context.applicationInfo.name, 0).path)
    //允许WebView使用File协议
    settings.allowFileAccess = true
    //不保存密码
    settings.savePassword = false
    //设置UA
    settings.userAgentString =
        (settings.userAgentString + context.applicationInfo.name)
    //自动加载图片
    settings.loadsImagesAutomatically = true
    removeJavascriptInterfaces()

}

fun WebView.onClient(titleCallback: ((title: String?) -> Unit)? = null) {
    webViewClient = SafeWebViewClient()
    webChromeClient = object : SafeWebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            titleCallback?.invoke(title)
            super.onReceivedTitle(view, title)
        }
    }
}

////移除部分系统JavaScript接口
private fun WebView.removeJavascriptInterfaces() {
    try {
        if (Build.VERSION.SDK_INT in 11..16) {
            removeJavascriptInterface("searchBoxJavaBridge_")
            removeJavascriptInterface("accessibility")
            removeJavascriptInterface("accessibilityTraversal")
        }
    } catch (tr: Throwable) {
        tr.printStackTrace()
    }

}