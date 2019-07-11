package com.grantgz.baseapp.http

import com.adazhdw.baselibrary.http.RetrofitFactory

class ApiRetrofit : RetrofitFactory<ApiService>(){
    override fun getService(): Class<ApiService> {
        return ApiService::class.java
    }

    override fun baseUrl(): String {
        return "https://wanandroid.com"
    }
}

val apiService by lazy { ApiRetrofit().apiService }