package com.adazhdw.ktlib.kthttp.coroutines

import com.adazhdw.ktlib.kthttp.coroutines.parser.NormalParser
import com.adazhdw.ktlib.kthttp.coroutines.parser.Parser
import com.adazhdw.ktlib.kthttp.request.base.BaseRequest

/**
 * author：daguozhu
 * date-time：2020/11/18 13:37
 * description： BaseRequest,协程 await 方法
 **/

fun <T> BaseRequest.awaitImpl(
    parser: Parser<T>
): IAwait<T> = IAwaitImpl(this, parser)

inline fun <reified T : Any> BaseRequest.toClazz() = awaitImpl(object : NormalParser<T>() {})