package com.adazhdw.libapp

import com.adazhdw.kthttp.BuildConfig
import com.adazhdw.ktlib.Application
import com.adazhdw.ktlib.core.delegate.DelegateExt
import com.adazhdw.libapp.net.CoroutineCallAdapterFactory
import com.adazhdw.libapp.net.GsonConverterFactory
import com.adazhdw.libapp.net.OkHttpClientFactory
import com.adazhdw.net.Net

/**
 * author：daguozhu
 * date-time：2020/11/13 16:57
 * description：
 **/
class LibApp : Application() {

    override val isDebug: Boolean
        get() = BuildConfig.DEBUG

    companion object {
        var instance: LibApp by DelegateExt.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
val net: Net by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    Net.Builder()
        .baseUrl(C.BASE_URL)
        .client(OkHttpClientFactory(LibApp.instance.applicationContext).create())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}
