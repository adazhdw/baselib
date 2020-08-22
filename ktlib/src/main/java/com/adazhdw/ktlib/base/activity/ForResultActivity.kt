package com.adazhdw.ktlib.base.activity

import android.content.Intent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

abstract class ForResultActivity : NetCallbackActivity() {

    private val resultCallbackSet = mutableMapOf<Int, ((resultCode: Int, data: Intent?) -> Unit)>()
    fun startActivityForResultCompact(
        intent: Intent,
        resultCallback: ((resultCode: Int, data: Intent?) -> Unit)
    ) {
        currentCode = requestCode()
        resultCallbackSet[currentCode] = resultCallback
        startActivityForResult(intent, currentCode)
    }

    suspend fun startActivityForResultCoroutines(
        intent: Intent,
        onFailure: (() -> Unit)? = null,
        onCancel: (() -> Unit)? = null
    ): Intent? =
        try {
            suspendCancellableCoroutine<Intent> { continuation ->
                startActivityForResultCompact(intent) { resultCode, data ->
                    when (resultCode) {
                        RESULT_OK -> {
                            if (data != null) continuation.resume(data)
                            else onFailure?.invoke()
                        }
                        RESULT_CANCELED -> onCancel?.invoke()
                        else -> continuation.resumeWithException(CancellationException())
                    }
                }
            }
        } catch (ex: CancellationException) {
            onFailure?.invoke()
            throw ex
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