package com.adazhdw.ktlib.kthttp.constant


sealed class Method(val name: String)
object GET : Method("GET")
object POST : Method("POST")
object DELETE : Method("DELETE")
object HEAD : Method("HEAD")
object PUT : Method("PUT")
object PATCH : Method("PATCH")