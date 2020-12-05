package com.adazhdw.ktlib.kthttp.coroutines

import com.adazhdw.ktlib.kthttp.coroutines.parser.Parser
import com.adazhdw.ktlib.kthttp.request.base.BaseRequest

/**
 * author：daguozhu
 * date-time：2020/11/18 13:29
 * description：
 **/
class IAwaitImpl<T>(
    private val baseRequest: BaseRequest,
    private val parser: Parser<T>
) : IAwait<T> {
    override suspend fun await(): T {
        return try {
            val call = baseRequest.getRawCall()
            call.await(parser)
        } catch (t: Throwable) {
            t.printStackTrace()
            throw t
        }
    }
}