package com.adazhdw.libapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.adazhdw.kthttp.coroutines.toClazz
import com.adazhdw.kthttp.getRequest
import com.adazhdw.ktlib.ext.dp2px
import com.adazhdw.ktlib.ext.toast
import com.adazhdw.ktlib.list.ListFragment
import com.adazhdw.ktlib.list.BaseVBAdapter
import com.adazhdw.ktlib.list.BaseVBViewHolder
import com.adazhdw.ktlib.list.view.LoadMoreRecyclerView
import com.adazhdw.ktlib.list.view.LoadMoreViewImpl
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
    }*/

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun rvExtra(recyclerView: LoadMoreRecyclerView) {
        recyclerView.setLoadMoreView(LoadMoreViewImpl(recyclerView.context))
        val footer = layoutInflater.inflate(R.layout.base_header, null, false)
        val header = layoutInflater.inflate(R.layout.base_header, null, false)
        recyclerView.addHeaderView(header, dp2px(50f))
        recyclerView.addFooterView(footer, dp2px(50f))
    }

    override fun getDataAdapter(): ChaptersAdapter = ChaptersAdapter().apply {
        setItemClickListener { holder, data, position ->
            toast(data.toString())
        }
    }

    override fun onLoad(page: Int, callback: LoadDataCallback<WxArticleChapter>) {
        launchOnUI {
            val url = "https://wanandroid.com/wxarticle/chapters/json"
            val data = getRequest { url(url) }.toClazz<ListResponse<WxArticleChapter>>().await().data ?: listOf()
            val hasmore = dataSize < 16
            callback.onSuccess(data, hasmore)
        }
    }

    /*override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2)
    }*/
}

class ChaptersAdapter : BaseVBAdapter<WxArticleChapter>() {

    override fun viewBinding(parent: ViewGroup, inflater: LayoutInflater, viewType: Int): ViewBinding {
        return NetChapterItemBinding.inflate(inflater, parent, false)
    }

    override fun notifyBindVH(holder: BaseVBViewHolder, data: WxArticleChapter, position: Int) {
        (holder.viewBinding as NetChapterItemBinding).chapterName.text = data.chapterName
    }

}



