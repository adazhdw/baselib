package com.adazhdw.kthttp.coroutines

/**
 * author：daguozhu
 * date-time：2020/11/18 11:27
 * description：
 **/
interface IAwait<T> {
    suspend fun await(): T
}

