package com.adazhdw.ktlib.list

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.adazhdw.ktlib.R
import com.adazhdw.ktlib.ext.dp2px

class DefaultLoadMoreView : FrameLayout, LoadMoreView {

    private val loadTv: TextView

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val loadView = LayoutInflater.from(context).inflate(R.layout.fragment_list_footer, null, false)
        loadTv = loadView.findViewById(R.id.loadTv)
        this.addView(loadView)
    }

    override fun onLoading() {
        loadTv.text = "加载中"
    }

    override fun onLoadFinish(dataEmpty: Boolean, hasMore: Boolean) {
        loadTv.text = "加载完成"
    }

    override fun onWaitToLoadMore(loadMoreListener: ListRecyclerView.LoadMoreListener) {

    }

    override fun onLoadError(errorCode: Int, errorMsg: String?) {
        loadTv.text = "$errorMsg"
    }
}