package com.adazhdw.ktlib.kthttp

import com.adazhdw.ktlib.kthttp.converter.GsonConverter.Companion.create
import com.adazhdw.ktlib.kthttp.converter.IConverter

/**
 * author：daguozhu
 * date-time：2020/11/3 19:38
 * description：
 */
object KtConfig {
    var converter: IConverter = create()
    var needDecodeResult: Boolean = false
}