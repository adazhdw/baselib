package com.adazhdw.baselibrary.widget.webview

import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.*


open class SafeWebViewClient : WebViewClient() {
    /**
     * 当webView页面Scale值发生变化时回调
     */
    override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
        super.onScaleChanged(view, oldScale, newScale)
    }

    /**
     * 是否在webView内加载页面
     */
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        view?.loadUrl(url)
        return true
    }

    /**
     * webView 开始加载页面时回调，一次Frame加载对应一次回调
     */
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
    }

    /**
     * webView 完成加载页面时回调，一次Frame加载对应一次回调
     */
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
    }


    /**
     * WebView 加载页面资源时回调，每一次资源产生的一次网络加载，除非本地有当前url对应缓存，否则就会加载
     */
    override fun onLoadResource(view: WebView?, url: String?) {
        super.onLoadResource(view, url)
    }

    /**
     * WebView 可以拦截某一次的 request 来返回我们自己加载的数据，这个方法会在缓存时有大作用
     */
    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        return super.shouldInterceptRequest(view, request)
    }

    /**
     * WebView 加载url出错
     */
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
    }

    /**
     * WebView ssl 访问证书出错，handler.cancel()取消加载，handler.processed()有错误也继续加载
     */
    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        super.onReceivedSslError(view, handler, error)
    }

}