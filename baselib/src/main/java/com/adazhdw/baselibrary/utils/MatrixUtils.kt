package com.adazhdw.baselibrary.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable

object MatrixUtils {

    /**
     * 图片旋转
     */
    fun matrixBitmap(context: Context, res: Int, degrees: Int = -90): BitmapDrawable {
        val bitmapOrg = BitmapFactory.decodeResource(context.resources, res)
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())//旋转的角度

        val resizedBitmap = Bitmap.createBitmap(
            bitmapOrg, 0, 0,
            bitmapOrg.width, bitmapOrg.height, matrix, true
        )
        return BitmapDrawable(context.resources, resizedBitmap)
    }
}
