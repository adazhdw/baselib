package com.adazhdw.kthttp.constant


/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description:
 */

sealed class Method(val name: String) {

    object GET : Method("GET")
    object POST : Method("POST")
    object DELETE : Method("DELETE")
    object HEAD : Method("HEAD")
    object PUT : Method("PUT")
    object PATCH : Method("PATCH")
}