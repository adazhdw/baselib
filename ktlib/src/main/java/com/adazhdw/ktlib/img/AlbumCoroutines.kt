package com.adazhdw.ktlib.img

import com.adazhdw.ktlib.base.ForResultActivity
import com.adazhdw.ktlib.ext.logE
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


suspend fun ForResultActivity.selectImageCoroutines(
    onError: ((String) -> Unit)? = null,
    onCancel: (() -> Unit)? = null
): DocumentModel =
    try {
        suspendCancellableCoroutine<DocumentModel> { continuation ->
            selectImage(
                onResult = {
                    continuation.resume(it)
                },
                onDenied = {
                    onError?.invoke("permission denied")
                }, onError = {
                    onError?.invoke(it)
                    it.logE()
                    continuation.resumeWithException(CancellationException(it))
                }, onCancel = {
                    onCancel?.invoke()
                }
            )
        }
    } catch (ex: CancellationException) {
        ex.message.logE()
        throw ex
    }

suspend fun ForResultActivity.captureImageCoroutines(
    onError: ((String) -> Unit)? = null,
    onCancel: (() -> Unit)? = null
): DocumentModel =
    try {
        suspendCancellableCoroutine<DocumentModel> { continuation ->
            captureImage(
                onResult = {
                    continuation.resume(it)
                },
                onDenied = {
                    onError?.invoke("permission denied")
                },
                onError = {
                    onError?.invoke(it)
                    continuation.resumeWithException(CancellationException(it))
                }, onCancel = {
                    onCancel?.invoke()
                }
            )
        }
    } catch (ex: CancellationException) {
        ex.message.logE()
        throw ex
    }
