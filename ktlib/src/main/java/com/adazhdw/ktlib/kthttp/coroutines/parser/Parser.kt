package com.adazhdw.ktlib.kthttp.coroutines.parser

import okhttp3.Response

/**
 * author：daguozhu
 * date-time：2020/11/18 10:58
 * description：
 **/

interface Parser<T> {

    fun parse(response: Response): T

}