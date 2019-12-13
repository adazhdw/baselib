package com.adazhdw.ktlib.base.mvvm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRepository {

    suspend fun <T> apiCall(apiFun: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            apiFun.invoke()
        }
    }
}