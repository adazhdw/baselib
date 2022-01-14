package com.adazhdw.net


/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description:
 */

sealed class HttpMethod(val name: String, val hasBody: Boolean) {

    object GET : HttpMethod("GET", false)
    object DELETE : HttpMethod("DELETE", false)
    object HEAD : HttpMethod("HEAD", false)
    object POST : HttpMethod("POST", true)
    object PUT : HttpMethod("PUT", true)
    object PATCH : HttpMethod("PATCH", true)
    object OPTIONS : HttpMethod("OPTIONS", false)
}