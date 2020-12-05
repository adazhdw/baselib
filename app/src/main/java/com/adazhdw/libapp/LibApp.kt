package com.adazhdw.libapp

import com.adazhdw.ktlib.Application

/**
 * author：daguozhu
 * date-time：2020/11/13 16:57
 * description：
 **/
class LibApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }
}