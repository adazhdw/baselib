package com.adazhdw.kthttp.coder

/**
 * author：daguozhu
 * date-time：2020/11/6 15:45
 * description：
 **/
interface ICoder {
    fun encode(string: String): String

    fun decode(string: String): String
}