@file:Suppress("NOTHING_TO_INLINE","unused")

package com.adazhdw.ktlib.ext

import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat

/**
 * author: daguozhu
 * created on: 2019/10/24 17:18
 * description:
 */

inline fun String.parseAsHtml(
    flags: Int = HtmlCompat.FROM_HTML_MODE_LEGACY,
    imageGetter: Html.ImageGetter? = null,
    tagHandler: Html.TagHandler? = null
): Spanned = HtmlCompat.fromHtml(this, flags, imageGetter, tagHandler)



inline fun Spanned.toHtml(
    option: Int = HtmlCompat.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE
): String = HtmlCompat.toHtml(this, option)