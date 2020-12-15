package com.adazhdw.libapp.ui.notifications

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.adazhdw.kthttp.KtHttp
import com.adazhdw.kthttp.coroutines.toClazz
import com.adazhdw.ktlib.list.ListFragment
import com.adazhdw.ktlib.list.adapter.ViewBindingAdapter
import com.adazhdw.ktlib.list.holder.BaseVBViewHolder
import com.adazhdw.libapp.bean.ListResponse
import com.adazhdw.libapp.bean.WxArticleChapter
import com.adazhdw.libapp.databinding.NetChapterItemBinding

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
            val url = "https://wanandroid.com/wxarticle/chapters/json"
            val data = KtHttp.ktHttp.get(url).toClazz<ListResponse<WxArticleChapter>>().await().data ?: listOf()
            val hasmore = dataSize < 25
            callback.onSuccess(data, hasmore)
        }
    }
}

class ChaptersAdapter() : ViewBindingAdapter<WxArticleChapter>() {

    override fun viewBinding(parent: ViewGroup, viewType: Int): ViewBinding {
        return NetChapterItemBinding.inflate(inflater, parent, false)
    }

    override fun notifyBind(holder: BaseVBViewHolder, position: Int) {
        (holder.viewBinding as NetChapterItemBinding).chapterName.text = getItem(position).chapterName
    }

}



