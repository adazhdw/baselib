package com.grantgz.baseapp.http

import com.adazhdw.ktlib.http.RetrofitClient
import com.grantgz.baseapp.BuildConfig

val apiService by lazy { RetrofitClient.create<ApiService>() }