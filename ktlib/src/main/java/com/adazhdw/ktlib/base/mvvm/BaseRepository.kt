package com.adazhdw.ktlib.base.mvvm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseRepository {

    suspend fun <T> launchOnIO(block: suspend () -> T): T {
        return withContext(Dispatchers.IO) { block.invoke() }
    }
}