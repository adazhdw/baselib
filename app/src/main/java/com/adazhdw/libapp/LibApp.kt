package com.adazhdw.libapp

import com.adazhdw.kthttp.BuildConfig
import com.adazhdw.ktlib.Application
import com.adazhdw.ktlib.core.delegate.DelegateExt

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
