package com.adazhdw.ktlib.kthttp

import com.adazhdw.ktlib.kthttp.coder.ICoder
import com.adazhdw.ktlib.kthttp.coder.UrlCoder
import com.adazhdw.ktlib.kthttp.converter.GsonConverter
import com.adazhdw.ktlib.kthttp.converter.IConverter

/**
 * author：daguozhu
 * date-time：2020/11/3 19:38
 * description：
 */
object KtConfig {
    var converter: IConverter = GsonConverter.create()
    var coder: ICoder = UrlCoder.create()
    var needDecodeResult: Boolean = false
}