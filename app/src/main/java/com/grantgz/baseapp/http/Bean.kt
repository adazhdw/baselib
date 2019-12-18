package com.grantgz.baseapp.http


data class BaseResponse<T>(
    val errorCode: Int = 0,
    val errorMsg: String = "",
    val data: T? = null
)

data class ListResponse<T>(
    val errorCode: Int = 0,
    val errorMsg: String = "",
    val data: List<T>?
)


data class WxArticleChapter(
    var children: List<String>? = null,
    var courseId: Int = 0,
    var id: Int = 0,
    var name: String? = null,
    var order: Int = 0,
    var parentChapterId: Int = 0,
    var userControlSetTop: Boolean = false,
    var visible: Int = 0
)

data class ChapterHistory(
    val apkLink: String? = null,
    val author: String? = null,
    val id: Int? = null,
    val chapterId: Int? = null,
    val chapterName: String? = null,
    val collect: Boolean = false,
    val courseId: Int? = null,
    val link: String? = null,
    val niceDate: String? = null,
    val origin: String? = null,
    val projectLink: String? = null,
    val publishTime: Long? = null,
    val superChapterId: Int? = null,
    val superChapterName: String? = null,
    val title: String? = null,
    val type: Int? = null,
    val userId: Int? = null,
    val visible: Int? = null,
    val zan: Int? = null,
    val tags: List<HistoryTag>? = null
)

data class HistoryTag(
    val name: String? = null,
    val url: String? = null
)

data class HistoryData(
    val datas: List<ChapterHistory>? = null
)

data class HotKey(
    val id: Int,
    val name: String?
)



