package com.adazhdw.ktlib.base.fragment

import androidx.fragment.app.Fragment
import com.adazhdw.ktlib.ext.logE
import kotlinx.coroutines.*

abstract class CoroutinesFragment : Fragment(), CoroutineScope by MainScope() {

    protected val TAG = this.javaClass.simpleName + "------"

    override fun onDestroyView() {
        super.onDestroyView()
        cancel()
    }

    protected fun launchOnUI(
        error: ((Exception) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        launch {
            tryCatch(block, {
                error?.invoke(it)
                "error:${it.message}".logE(TAG)
            }, {}, true)
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