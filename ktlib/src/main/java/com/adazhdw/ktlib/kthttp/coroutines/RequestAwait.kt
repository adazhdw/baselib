package com.adazhdw.ktlib.kthttp.coroutines

import com.adazhdw.ktlib.kthttp.converter.parser.IParser
import com.adazhdw.ktlib.kthttp.converter.parser.NormalParser
import com.adazhdw.ktlib.kthttp.request.base.BaseRequest

/**
 * author：daguozhu
 * date-time：2020/11/18 13:37
 * description：
 **/

fun <T> BaseRequest.awaitImpl(parser: IParser<T>): IAwait<T> = IAwaitImpl(this, parser)

inline fun <reified T : Any> BaseRequest.toResponse() = awaitImpl(object : NormalParser<T>() {})