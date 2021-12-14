package com.adazhdw.libapp

import com.adazhdw.kthttp.BuildConfig
import com.adazhdw.ktlib.Application

/**
 * author：daguozhu
 * date-time：2020/11/13 16:57
 * description：
 **/
class LibApp : Application() {

    override val isDebug: Boolean
        get() = BuildConfig.DEBUG

    override fun onCreate() {
        super.onCreate()
    }
}