package com.adazhdw.ktlib.widget.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.adazhdw.ktlib.R
import com.adazhdw.ktlib.databinding.BaseFooterBinding
import com.adazhdw.ktlib.ext.view.invisible
import com.adazhdw.ktlib.ext.view.visible


/**
 * Administrator
 * create at 2020/4/8 17:10
 * description:
 */
abstract class LoadMoreAdapter<T> : BaseVBAdapter<T>() {

    private var mLoadState: Int = LoadState.LOAD_ALL

    final override fun viewBinding(parent: ViewGroup, viewType: Int): ViewBinding {
        if (viewType == footerId()) return footerBinding(parent, viewType)
        if (viewType == headerId()) {
            return headerBinding(parent, viewType)
                ?: throw RuntimeException("headerBinding is null")
        }
        return itemBinding(parent, viewType)
    }

    override fun convert(holder: BaseVBViewHolder, item: T) {
        val position = holder.adapterPosition
        if (data.isEmpty() || position == RecyclerView.NO_POSITION) return
        when {
            isFooter(position) -> bindFooter(holder)
            isHeader(position) -> bindHeader(holder)
            else -> {
                if (data.isEmpty()) return
                bindHolder(holder, data[position - headerCount()], position - headerCount())
            }
        }
    }

    override fun getItemCount(): Int {
        return if (data.isNotEmpty()) {
            data.size + if (needFooter()) 1 else 0 + headerCount()/*header和footer数量*/
        } else 0
    }

    override fun getItemViewType(position: Int): Int {
        if (isHeader(position)) return headerId()
        if (isFooter(position)) return footerId()
        return super.getItemViewType(position)
    }

    private fun isFooter(position: Int) = position == data.size + headerCount() && needFooter()
    private fun isHeader(position: Int) = position == 0 && headerCount() == 1

    open fun headerBinding(parent: ViewGroup, viewType: Int): ViewBinding? = null
    open fun footerBinding(parent: ViewGroup, viewType: Int): ViewBinding =
        BaseFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    abstract fun itemBinding(parent: ViewGroup, viewType: Int): ViewBinding

    open fun needFooter() = true
    open fun needHeader() = false
    open fun footerId() = R.layout.base_footer
    open fun headerId() = R.layout.base_header
    open fun headerCount() = 0
    open fun bindHeader(holder: BaseVBViewHolder) {}
    open fun bindFooter(holder: BaseVBViewHolder) {
        (holder.viewBinding as BaseFooterBinding).let {
            when {
                isLoadAll() -> {
                    it.loadProgress.invisible()
                    it.loadTip.visible()
                    it.loadTip.text = "加载完成"
                }
                isLoadError() -> {
                    it.loadProgress.invisible()
                    it.loadTip.visible()
                    it.loadTip.text = "加载出错"
                }
                else -> {
                    it.loadProgress.visible()
                    it.loadTip.invisible()
                }
            }
        }
    }

    /**
     * Footer 状态判断和更改
     */
    fun loading() {
        mLoadState = LoadState.LOADING
        notifyItemChanged(itemCount - 1)
    }

    fun loadError() {
        mLoadState = LoadState.ERROR
        notifyItemChanged(itemCount - 1)
    }

    fun loadAll() {
        mLoadState = LoadState.LOAD_ALL
        notifyItemChanged(itemCount - 1)
    }

    private fun isLoadAll() = mLoadState == LoadState.LOAD_ALL
    private fun isLoadError() = mLoadState == LoadState.ERROR

}

object LoadState {
    const val LOADING = 1
    const val ERROR = 2
    const val LOAD_ALL = 3
}