package com.adazhdw.ktlib.base.mvvm


sealed class VMNetState

object Success : VMNetState()

class Error(val code: String = "", val msg: String = "", val error: Throwable = Exception()) :
    VMNetState()

object Loading : VMNetState()