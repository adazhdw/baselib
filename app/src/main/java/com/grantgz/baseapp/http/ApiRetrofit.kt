package com.grantgz.baseapp.http

import com.adazhdw.ktlib.http.RetrofitClient

val apiService by lazy { RetrofitClient.create<ApiService>() }