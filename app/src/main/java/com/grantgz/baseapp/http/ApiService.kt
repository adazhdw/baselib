package com.grantgz.baseapp.http

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/wxarticle/chapters/json")
    fun getWxArticleChapters2(): Call<ListResponse<WxArticleChapter>>

    @GET("/wxarticle/chapters/json")
    suspend fun getWxArticleChapters3(): ListResponse<WxArticleChapter>

    @GET("/wxarticle/list/{articleId}/{page}/json")
    fun getWxArticleHistory2(@Path("articleId") articleId: Int, @Path("page") page: Int = 1): Call<BaseResponse<HistoryData>>

    @GET("/wxarticle/list/{articleId}/{page}/json")
    fun getWxArticleHistoryAsync(@Path("articleId") articleId: Int, @Path("page") page: Int = 1): Deferred<BaseResponse<HistoryData>>

    @GET("/hotkey/json")
    fun getHotKey(): Call<ListResponse<HotKey>>
}
