package com.adazhdw.libapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.adazhdw.ktlib.list.ListFragment
import com.adazhdw.ktlib.list.adapter.ViewBindingAdapter
import com.adazhdw.ktlib.list.holder.BaseVBViewHolder
import com.adazhdw.libapp.bean.ListResponse
import com.adazhdw.libapp.bean.WxArticleChapter
import com.adazhdw.libapp.databinding.NetChapterItemBinding
import com.adazhdw.net.await

/**
 * author：daguozhu
 * date-time：2020/12/10 17:17
 * description：
 **/

class WxChaptersFragment : ListFragment<WxArticleChapter, ChaptersAdapter>() {
    // IM模式，最新消息在最底，通过下拉到顶部，加载历史消息
    /*override fun rvExtra(recyclerView: LoadMoreRecyclerView) {
        recyclerView.canScrollDirection(LoadMoreRecyclerView.SCROLL_DIRECTION_TOP)
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
    }*/

    override fun getDataAdapter(): ChaptersAdapter = ChaptersAdapter()

    override fun onLoad(page: Int, callback: LoadDataCallback<WxArticleChapter>) {
        launchOnUI {
            /*val url = "https://wanandroid.com/wxarticle/chapters/json"
            val data =  getRequest{ url(url) }.toClazz<ListResponse<WxArticleChapter>>().await().data ?: listOf()*/
            val data = net.get("wxarticle/chapters/json").parseClazz<ListResponse<WxArticleChapter>>().await().data ?: listOf()
            val hasmore = dataSize < 20
            callback.onSuccess(data, hasmore)
        }
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2)
    }
}

class ChaptersAdapter : ViewBindingAdapter<WxArticleChapter>() {

    override fun viewBinding(parent: ViewGroup, inflater: LayoutInflater, viewType: Int): ViewBinding {
        return NetChapterItemBinding.inflate(inflater, parent, false)
    }

    override fun notifyBind(holder: BaseVBViewHolder, data: WxArticleChapter, position: Int) {
        (holder.viewBinding as NetChapterItemBinding).chapterName.text = data.chapterName
    }

}



