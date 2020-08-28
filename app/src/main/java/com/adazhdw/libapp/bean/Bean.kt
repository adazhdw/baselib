package com.adazhdw.libapp.bean

/**
 * Author: dgz
 * Date: 2020/8/27 14:14
 * Description:
 */

data class DatasBean(
    val niceDate: String,
    val title: String,
    val desc: String,
    val author: String,
    val shareUser: String,
    val id: Int,
    val chapterName: String,
    val link: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DatasBean

        if (niceDate != other.niceDate) return false
        if (title != other.title) return false
        if (desc != other.desc) return false
        if (author != other.author) return false
        if (shareUser != other.shareUser) return false
        if (id != other.id) return false
        if (chapterName != other.chapterName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = niceDate.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + desc.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + shareUser.hashCode()
        result = 31 * result + id
        result = 31 * result + chapterName.hashCode()
        return result
    }
}


data class DataFeed(
    val curPage: Int,
    val offset: Int,
    val pageCount: Int,
    val size: Int,
    val total: Int,
    val over: Boolean,
    val datas: MutableList<DatasBean>
)

data class NetResponse<T>(val data: T, val errorCode: Int, val errorMsg: String)
