package com.grantgz.baseapp.http

import com.adazhdw.baselibrary.http.RetrofitFactory
import com.grantgz.baseapp.BuildConfig

class ApiRetrofit : RetrofitFactory<ApiService>(){
    override fun getService(): Class<ApiService> {
        return ApiService::class.java
    }

    override fun baseUrl(): String {
        return BuildConfig.DOMAIN
    }
}

val apiService by lazy { ApiRetrofit().apiService }