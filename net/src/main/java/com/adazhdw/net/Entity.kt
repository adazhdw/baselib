package com.adazhdw.net


data class ParamField(
    val name: String,
    val value: String,
    val encoded: Boolean
)

data class PartField(
    val name: String,
    val value: okhttp3.RequestBody,
    val encoding: String = "binary"
)