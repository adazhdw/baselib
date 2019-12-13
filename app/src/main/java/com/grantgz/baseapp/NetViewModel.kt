package com.grantgz.baseapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adazhdw.ktlib.base.mvvm.BaseRepository
import com.adazhdw.ktlib.base.mvvm.BaseViewModel
import com.adazhdw.ktlib.http.await
import com.grantgz.baseapp.http.*


class NetViewModel : BaseViewModel<NetRepository>() {
    override fun obtainRepository(): NetRepository {
        return NetRepository()
    }

    val mHotKeyList = MutableLiveData<List<HotKey>>()

    val mChapterList = MutableLiveData<List<WxArticleChapter>>()

    val mHistoryData = MutableLiveData<HistoryData>()

    fun getHotKey() {
        launch {
            mHotKeyList.postValue(mRepository.getHotKey().data)
        }
    }

    fun getWxArticleChapters2() {
        launch {
            mChapterList.postValue(mRepository.getWxArticleChapters2().data)
        }
    }

    fun getWxArticleHistory2(articleId: Int, page: Int = 1) {
        launch {
            mHistoryData.postValue(mRepository.getWxArticleHistory2(articleId, page).data)
        }
    }

}

class NetRepository : BaseRepository() {
    suspend fun getHotKey(): ListResponse<HotKey> {
        return apiCall {
            apiService.getHotKey().await()
        }
    }

    suspend fun getWxArticleChapters2(): ListResponse<WxArticleChapter> {
        return apiCall {
            apiService.getWxArticleChapters2().await()
        }
    }

    suspend fun getWxArticleHistory2(articleId: Int, page: Int = 1): BaseResponse<HistoryData> {
        return apiCall {
            apiService.getWxArticleHistory2(articleId, page).await()
        }
    }
}

class NetModelFactory : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NetViewModel() as T
    }
}