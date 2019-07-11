package com.adazhdw.baselibrary.utils

import android.os.Environment
import android.os.Environment.*
import android.os.StatFs

object StorageUtils {

    fun getExStorageSize(): BlockSize {
        /** 获取存储卡路径  */
        val sdcardDir = getExternalStorageDirectory()

        /** StatFs 看文件系统空间使用情况  */
        val statFs = StatFs(sdcardDir.path)

        /** Block 的 size */
        val blockSize = statFs.blockSizeLong

        /** 总 Block 数量  */
        val totalBlocks = statFs.blockCountLong

        /** 已使用的 Block 数量  */
        val availableBlocks = statFs.availableBlocksLong

        return BlockSize(blockSize, totalBlocks, availableBlocks)
    }

    fun getSDPath():String{
        val exists:Boolean = getExternalStorageState() == MEDIA_MOUNTED
        return if (exists) {
            Environment.getExternalStorageDirectory().absolutePath
        }else{
            ""
        }
    }
}

data class BlockSize(val blockSize:Long,val totalBlocks:Long,val availableBlocks:Long)
