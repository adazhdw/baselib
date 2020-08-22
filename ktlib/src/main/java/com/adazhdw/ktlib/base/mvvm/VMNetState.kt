package com.adazhdw.ktlib.base.mvvm


sealed class VMNetState

object NetSuccess : VMNetState()
class NetError(val code: String = "", val msg: String = "", val error: Throwable = Exception()) :
    VMNetState()

object NetLoading : VMNetState()