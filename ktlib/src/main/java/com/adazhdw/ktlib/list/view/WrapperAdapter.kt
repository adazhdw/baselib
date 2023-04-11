package com.adazhdw.ktlib.list.view

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

open class WrapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindVH() {}
}

class HeaderVH(itemView: View) : WrapViewHolder(itemView)
class FooterVH(itemView: View) : WrapViewHolder(itemView)
class LoadMoreVH(itemView: View) : WrapViewHolder(itemView)


class WrapperAdapter(internal val innerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, private val wrapperView: IWrapperView) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun innerItemCount() = innerAdapter.itemCount

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_LOAD_MORE) {
            return LoadMoreVH(wrapperView.loadMoreView().getContentView())
        } else if (viewType == VIEW_TYPE_FOOTER && wrapperView.footerNotEmpty) {
            return FooterVH(wrapperView.footerView())
        } else if (viewType == VIEW_TYPE_HEAD && wrapperView.headerNotEmpty) {
            return HeaderVH(wrapperView.headerView())
        }
        return innerAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        when (holder) {
            is HeaderVH -> {
                holder.bindVH()
            }
            is FooterVH -> {
                holder.bindVH()
            }
            is LoadMoreVH -> {
                holder.bindVH()
            }
            else -> {
                innerAdapter.onBindViewHolder(holder, position - wrapperView.headerCount)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (isHeaderPos(position)) return VIEW_TYPE_HEAD
        if (isFooterPos(position)) return VIEW_TYPE_FOOTER
        if (isLoadMorePos(position)) return VIEW_TYPE_LOAD_MORE
        return innerAdapter.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return innerItemCount() + wrapperView.loadMoreViewCount + wrapperView.headerCount + wrapperView.footerCount
    }

    private fun isLoadMorePos(position: Int): Boolean {
        return wrapperView.isLoadMoreEnabled() && position == (itemCount - wrapperView.loadMoreViewCount)
    }

    private fun isHeaderPos(position: Int): Boolean {
        return wrapperView.headerNotEmpty && position == 0
    }

    private fun isFooterPos(position: Int): Boolean {
        return wrapperView.footerNotEmpty && position == (itemCount - wrapperView.footerCount - wrapperView.loadMoreViewCount)
    }

    private fun isWrapperPos(position: Int): Boolean {
        return isHeaderPos(position) || isFooterPos(position) || isLoadMorePos(position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (isWrapperPos(position)) layoutManager.spanCount else 1
                }
            }
        }
        innerAdapter.onAttachedToRecyclerView(recyclerView)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val lp = holder.itemView.layoutParams
        val position = holder.layoutPosition
        if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
            if (isWrapperPos(position)) lp.isFullSpan = true
        }
        innerAdapter.onViewAttachedToWindow(holder)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        innerAdapter.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        innerAdapter.onViewDetachedFromWindow(holder)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        innerAdapter.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return innerAdapter.onFailedToRecycleView(holder)
    }

    override fun unregisterAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        innerAdapter.unregisterAdapterDataObserver(observer)
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        innerAdapter.registerAdapterDataObserver(observer)
    }
}