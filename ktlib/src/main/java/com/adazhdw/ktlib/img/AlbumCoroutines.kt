package com.adazhdw.ktlib.img

import com.adazhdw.ktlib.base.ForResultActivity
import com.adazhdw.ktlib.ext.loge

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
            selectImage2(
                onResult = {
                    continuation.resume(it)
                },
                onDenied = {

                },onError = {
                    onError?.invoke(it)
                    loge(content = it)
                    continuation.resumeWithException(CancellationException(it))
                }, onCancel = {
                    onCancel?.invoke()
                }
            )
        }
    } catch (ex: CancellationException) {
        loge(content = ex.message)
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
        loge(content = ex.message?:"")
        throw ex
    }
