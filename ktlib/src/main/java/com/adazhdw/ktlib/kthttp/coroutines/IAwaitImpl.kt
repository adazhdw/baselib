package com.adazhdw.ktlib.kthttp.coroutines

import com.adazhdw.ktlib.kthttp.coroutines.parser.IParser
import com.adazhdw.ktlib.kthttp.request.base.BaseRequest

/**
 * author：daguozhu
 * date-time：2020/11/18 13:29
 * description：
 **/
class IAwaitImpl<T>(
    private val baseRequest: BaseRequest,
    private val parser: IParser<T>
) : IAwait<T> {
    override suspend fun await(): T {
        val call = baseRequest.getRawCall()
        return try {
            call.await(parser)
        } catch (t: Throwable) {
            t.printStackTrace()
            throw t
        }
    }
}