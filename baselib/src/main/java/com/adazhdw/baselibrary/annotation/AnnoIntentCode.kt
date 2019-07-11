package com.adazhdw.baselibrary.annotation

import androidx.annotation.IntDef

/**
 * intent code annotation
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(
    IntentCode.REQUEST_PERMISSION_CODE,
    IntentCode.USER_CHANGE_PERMISSION_CODE,
    IntentCode.IMAGE_CAPTURE_CODE,
    IntentCode.VIDEO_CAPTURE_CODE
)
annotation class AnnoIntentCode

object IntentCode {
    const val REQUEST_PERMISSION_CODE = 1101
    const val USER_CHANGE_PERMISSION_CODE = 1102
    const val IMAGE_CAPTURE_CODE = 1103
    const val VIDEO_CAPTURE_CODE = 1104
}