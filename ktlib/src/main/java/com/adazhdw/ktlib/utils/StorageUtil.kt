package com.adazhdw.ktlib.utils

import android.os.Environment
import android.os.StatFs

fun getExternalStorageState(): String {
    return Environment.getExternalStorageState()
}

/**
 * 判断SD卡是否存在
 */
fun hasSDCard(): Boolean {
    val state = getExternalStorageState()
    return state == Environment.MEDIA_MOUNTED
}

/**
 * 判断SDCard是否可拆卸
 */
fun isSDCardRemovable(): Boolean {
    return Environment.isExternalStorageRemovable()
}

/**
 * 获取SD卡的完整空间大小，返回MB
 * 需要最小API18
 */
fun getSDCardSizeMB(): Long {
    if (isSDCardMounted()) {
        val fs = StatFs(getSDCardRootDir())
        val count = fs.blockCountLong
        val size = fs.blockSizeLong
        return count * size / 1024 / 1024
    }
    return 0
}

/**
 * 判断SD卡是否可用
 */
fun isSDCardMounted(): Boolean {
    return Environment.MEDIA_MOUNTED == getExternalStorageState()
}

/**
 * 获取SD卡的根目录
 *
 */
fun getSDCardRootDir(): String? {
    if (isSDCardMounted()) {
        return Environment.getExternalStorageDirectory().absolutePath
    }
    return null
}

/**
 * 获取SD卡的完整空间大小，返回KB
 * 需要最小API18
 */
fun getSDCardSizeKB(): Long {
    if (isSDCardMounted()) {
        val fs = StatFs(getSDCardRootDir())
        val count = fs.blockCountLong
        val size = fs.blockSizeLong
        return count * size / 1024
    }
    return 0
}

/**
 * 获取SD卡的完整空间大小，返回B
 * 需要最小API18
 */
fun getSDCardSizeB(): Long {
    if (isSDCardMounted()) {
        val fs = StatFs(getSDCardRootDir())
        val count = fs.blockCountLong
        val size = fs.blockSizeLong
        return count * size
    }
    return 0
}

/**
 * 获取SD卡的剩余空间大小
 */
fun getSDCardFreeSizeMB(): Long {
    if (isSDCardMounted()) {
        val fs = StatFs(getSDCardRootDir())
        val count = fs.freeBlocksLong
        val size = fs.blockSizeLong
        return count * size / 1024 / 1024
    }
    return 0
}

/**
 * 获取SD卡的剩余空间大小
 */
fun getSDCardFreeSizeKB(): Long {
    if (isSDCardMounted()) {
        val fs = StatFs(getSDCardRootDir())
        val count = fs.freeBlocksLong
        val size = fs.blockSizeLong
        return count * size / 1024
    }
    return 0
}

/**
 * 获取SD卡的剩余空间大小
 */
fun getSDCardFreeSizeB(): Long {
    if (isSDCardMounted()) {
        val fs = StatFs(getSDCardRootDir())
        val count = fs.freeBlocksLong
        val size = fs.blockSizeLong
        return count * size
    }
    return 0
}

/**
 * 获取SD卡的可用空间大小
 */
fun getSDCardFreeAvailableMB(): Long {
    if (isSDCardMounted()) {
        val fs = StatFs(getSDCardRootDir())
        val count = fs.availableBlocksLong
        val size = fs.blockSizeLong
        return count * size / 1024 / 1024
    }
    return 0
}

/**
 * 获取SD卡的可用空间大小
 */
fun getSDCardAvailableSizeKB(): Long {
    if (isSDCardMounted()) {
        val fs = StatFs(getSDCardRootDir())
        val count = fs.availableBlocksLong
        val size = fs.blockSizeLong
        return count * size / 1024
    }
    return 0
}

/**
 * 获取SD卡的可用空间大小
 */
fun getSDCardAvailableSizeB(): Long {
    if (isSDCardMounted()) {
        val fs = StatFs(getSDCardRootDir())
        val count = fs.availableBlocksLong
        val size = fs.blockSizeLong
        return count * size
    }
    return 0
}


