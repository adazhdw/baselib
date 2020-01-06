package com.grantgz.baseapp.http

import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.alibaba.fastjson.annotation.JSONType
import com.alibaba.fastjson.parser.Feature
import com.alibaba.fastjson.serializer.SerializerFeature


/**
 * Created by adazhdw on 2020/1/6.
 */

class FastJsonTest
fun main() {

    val dts = DataClassSimple(1, 2, listOf(DataClassSimple.Data2(3),DataClassSimple.Data2(4)))
    val jsons = JSONObject.toJSONString(dts)
    println(jsons)
    val clzs = DataClassSimple::class
    println(clzs.javaObjectType)
    val dt2 = parseObject<DataClassSimple>(jsons)
    println("-----------------------------")
    println(dt2)
    dt2.data.forEach {
        println(it.c)
    }
}

inline fun <reified T : Any> parseObject(json: String): T {
    return JSONObject.parseObject(json, object : TypeReference<T>() {})
}

data class DataClassSimple(val a: Int, val b: Int, val data: List<Data2>) {
    data class Data2(val c: Int)
}
