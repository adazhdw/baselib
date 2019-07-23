package com.adazhdw.baselibrary.base

import android.content.Intent

abstract class ForResultActivity : CoroutinesActivity() {

    private val resultCallbackSet = mutableMapOf<Int, ((resultCode: Int, data: Intent?) -> Unit)>()
    fun startActivityForResultCompat(
        intent: Intent,
        resultCallback: ((resultCode: Int, data: Intent?) -> Unit)
    ) {
        currentCode = requestCode()
        resultCallbackSet[currentCode] = resultCallback
        startActivityForResult(intent, currentCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == currentCode) {
            resultCallbackSet[currentCode]?.invoke(resultCode, data)
        }
    }


    private var currentCode = 0
    private val codeSet by lazy { hashSetOf(0) }
    private fun requestCode(): Int {
        var code = 0
        while (codeSet.contains(code)) {
            code += 1
        }
        return code
    }
}