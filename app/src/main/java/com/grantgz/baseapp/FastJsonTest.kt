package com.grantgz.baseapp

import com.alibaba.fastjson.JSON

/**
 * Created by adazhdw on 2020/1/3.
 */


fun main() {

    val dts = DataClassSimple(1, 2)
    val jsons = JSON.toJSONString(dts)
    println(jsons)
    val clzs = DataClassSimple::class
    println(clzs.javaObjectType)
    val dt2 = parseObject<DataClassSimple>(jsons)
    println("-----------------------------")
    println(dt2)
}

inline fun <reified T : Any> parseObject(json: String): T {
    val clz = T::class
    return JSON.parseObject(json, clz.javaObjectType)
}

data class DataClassSimple(val a: Int, val b: Int)
