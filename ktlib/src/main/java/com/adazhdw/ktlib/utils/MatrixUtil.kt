package com.adazhdw.ktlib.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import java.io.File

object MatrixUtil {

    /**
     * 图片旋转
     */
    fun matrixBitmapFromRes(context: Context, res: Int, degrees: Float = -90F): BitmapDrawable {
        val bitmapOrg = BitmapFactory.decodeResource(context.resources, res)
        val matrix = Matrix()
        matrix.postRotate(degrees)//旋转的角度

        val resizedBitmap = Bitmap.createBitmap(
            bitmapOrg, 0, 0,
            bitmapOrg.width, bitmapOrg.height, matrix, true
        )
        return BitmapDrawable(context.resources, resizedBitmap)
    }

    fun matrixBitmapFromFile(context: Context, file: File, degrees: Float = -90F): BitmapDrawable {
        val bitmapOrg = BitmapFactory.decodeFile(file.path)
        val matrix = Matrix()
        matrix.postRotate(degrees)//旋转的角度

        val resizedBitmap = Bitmap.createBitmap(
            bitmapOrg, 0, 0,
            bitmapOrg.width, bitmapOrg.height, matrix, true
        )
        return BitmapDrawable(context.resources, resizedBitmap)
    }
}
