package com.adazhdw.baselibrary.widget

import android.content.Context
import android.util.AttributeSet

class KolaWebView : WebViewImpl {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}