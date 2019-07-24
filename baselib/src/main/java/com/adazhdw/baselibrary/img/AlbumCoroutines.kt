package com.adazhdw.baselibrary.img

import com.adazhdw.baselibrary.base.ForResultActivity
import com.adazhdw.baselibrary.ext.logE
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


suspend fun ForResultActivity.selectImageCoroutines(
    onError: ((String) -> Unit)? = null,
    onCancel: (() -> Unit)? = null
): DocumentModel =
    try {
        suspendCancellableCoroutine { continuation ->
            selectImage(
                onResult = {
                    continuation.resume(it)
                }, onError = {
                    onError?.invoke(it)
                    logE(it)
                    continuation.resumeWithException(CancellationException(it))
                }, onCancel = {
                    onCancel?.invoke()
                }
            )
        }
    } catch (ex: CancellationException) {
        logE(ex.message)
        throw ex
    }

suspend fun ForResultActivity.captureImageCoroutines(
    onError: ((String) -> Unit)? = null,
    onCancel: (() -> Unit)? = null
): DocumentModel =
    try {
        suspendCancellableCoroutine { continuation ->
            captureImage(
                onResult = {
                    continuation.resume(it)
                }, onError = {
                    onError?.invoke(it)
                    continuation.resumeWithException(CancellationException(it))
                }, onCancel = {
                    onCancel?.invoke()
                }
            )
        }
    } catch (ex: CancellationException) {
        logE(ex.message?:"")
        throw ex
    }
