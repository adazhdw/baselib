package com.adazhdw.ktlib.widget.webview

import android.graphics.Bitmap
import android.webkit.*

open class SafeWebChromeClient :WebChromeClient() {

    /**
     * 输出 Web 端日志
     */
    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        return super.onConsoleMessage(consoleMessage)
    }

    /**
     * 当前 WebView 加载网页速度
     */
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
    }

    /**
     * Js 中调用 alert() 函数，产生的对话框
     */
    override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        return super.onJsAlert(view, url, message, result)
    }

    /**
     * 处理 Js 中的 confirm 对话框
     */
    override fun onJsConfirm(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        return super.onJsConfirm(view, url, message, result)
    }

    /**
     * 处理 Js 中的 Prompt 对话框
     */
    override fun onJsPrompt(
        view: WebView?,
        url: String?,
        message: String?,
        defaultValue: String?,
        result: JsPromptResult?
    ): Boolean {
        return super.onJsPrompt(view, url, message, defaultValue, result)
    }

    /**
     * 接受 Web 页面的 icon
     */
    override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
        super.onReceivedIcon(view, icon)
    }

    /**
     * 接受 Web 页面的 title
     */
    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
    }
}