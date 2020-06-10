package com.adazhdw.ktlib.base.activity

import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.adazhdw.ktlib.ext.logE
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

// version 1.0
abstract class CoroutinesActivity : AppCompatActivity() {

    protected val TAG = javaClass.name + "------"
    protected val mHandler: Handler by lazy { Handler(Looper.getMainLooper()) }

    protected fun launchWhenResumed(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launchWhenResumed(block)
    }

    protected fun launchWhenCreated(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launchWhenCreated(block)
    }

    protected fun launchWhenStarted(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launchWhenStarted(block)
    }

    fun launch(
        error: ((Exception) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) = launchOnUI(error, block)

    protected fun launchOnUI(
        error: ((Exception) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        lifecycleScope.launch {
            tryCatch(
                tryBlock = block,
                catchBlock = {
                    error?.invoke(it)
                    "error:${it.message}".logE(TAG)
                },
                finallyBlock = {},
                handleCancellationExceptionManually = true
            )
        }
    }

    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Exception) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean = false
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Exception) {
                if (e !is CancellationException || handleCancellationExceptionManually) {
                    catchBlock(e)
                } else {
                    throw e
                }
            } finally {
                finallyBlock()
            }
        }
    }
}