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
 * daguozhu
 * create at 2020/4/8 17:10
 * description:
 */
abstract class LoadMoreAdapter<T> : BaseVBAdapter<T>() {

    private var mLoadState: Int = LoadState.LOAD_ALL
    protected var inflater: LayoutInflater? = null

    /**
     * 所有的Binding
     */
    final override fun viewBinding(parent: ViewGroup, viewType: Int): ViewBinding {
        if (inflater == null) inflater = LayoutInflater.from(parent.context)
        if (viewType == footerId()) return getFooterBinding(inflater!!, parent)
        return getItemBinding(inflater!!, parent)
    }

    abstract fun getItemBinding(inflater: LayoutInflater, parent: ViewGroup): ViewBinding
    abstract fun bindItemHolder(holder: BaseVBViewHolder, data: T, position: Int)

    /**
     * 加载数据到Binding
     */
    override fun bindHolder(holder: BaseVBViewHolder, position: Int) {
        if (mData.isEmpty() || position == RecyclerView.NO_POSITION) return
        when {
            isFooter(position) -> bindFooter(holder.viewBinding as BaseFooterBinding)
            else -> bindItemHolder(holder, mData[position], position)
        }
    }

    override fun getItemCount(): Int {
        return if (mData.isNotEmpty()) (mData.size + if (needFooter()) 1 else 0) else 0
    }

    override fun getItemViewType(position: Int): Int {
        if (isFooter(position)) return footerId()
        return super.getItemViewType(position)
    }

    fun getData(): MutableList<T> = mData

    private fun isFooter(position: Int) = position == mData.size && needFooter()
    open fun needFooter() = true
    open fun footerId() = R.layout.base_footer

    open fun getFooterBinding(inflater: LayoutInflater, parent: ViewGroup): ViewBinding {
        return BaseFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    open fun bindFooter(viewBinding: BaseFooterBinding) {
        viewBinding.let {
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