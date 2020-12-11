package com.adazhdw.ktlib.list.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorInt
import com.adazhdw.ktlib.ext.dp2px
import com.adazhdw.ktlib.ext.view.invisible
import com.adazhdw.ktlib.ext.view.visible

class LoadMoreViewImpl : FrameLayout, LoadMoreView {

    private val loadTip: TextView
    private val loadProgress: ProgressBar
    private var mState: LoadMoreState = LoadMoreState.Loading
    private var loadingHint: String = "加载中"
    private var loadSuccessHint: String = "加载成功"
    private var loadErrorHint: String = "加载失败"
    private var textColor: Int = Color.parseColor("#888888")
    private var textSize = 14f

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.dp2px(60f))
        loadTip = TextView(context)
        loadTip.textSize = textSize
        loadTip.setTextColor(textColor)
        val tvParam = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply { gravity = Gravity.CENTER }
        addView(loadTip, tvParam)

        loadProgress = ProgressBar(context)
        val progressParam = LayoutParams(context.dp2px(20f), context.dp2px(20f)).apply { gravity = Gravity.CENTER }
        addView(loadProgress, progressParam)

        refreshLoadState()
    }

    private fun refreshLoadState() {
        when (mState) {
            LoadMoreState.Success -> {
                loadTip.visible()
                loadTip.text = loadSuccessHint
                loadProgress.invisible()
            }
            LoadMoreState.Error -> {
                loadTip.visible()
                loadTip.text = loadErrorHint
                loadProgress.invisible()
            }
            LoadMoreState.Loading -> {
                loadTip.invisible()
                loadTip.text = loadingHint
                loadProgress.visible()
            }
        }
    }

    fun setLoadingHint(hint: String): LoadMoreViewImpl {
        this.loadingHint = hint
        return this
    }

    fun setLoadSuccessHint(hint: String): LoadMoreViewImpl {
        this.loadSuccessHint = hint
        return this
    }

    fun setLoadErrorHint(hint: String): LoadMoreViewImpl {
        this.loadErrorHint = hint
        return this
    }

    fun setTextColor(@ColorInt color: Int): LoadMoreViewImpl {
        this.textColor = color
        return this
    }

    override fun loadSuccess() {
        mState = LoadMoreState.Success
        refreshLoadState()
    }

    override fun loadError() {
        mState = LoadMoreState.Error
        refreshLoadState()
    }

    override fun loading() {
        mState = LoadMoreState.Loading
        refreshLoadState()
    }

    override fun getContentView(): View {
        return this
    }
}