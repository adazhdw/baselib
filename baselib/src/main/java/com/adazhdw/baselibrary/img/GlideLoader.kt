package com.adazhdw.baselibrary.img

import android.content.Context
import android.widget.ImageView
import com.adazhdw.baselibrary.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

object GlideLoader {
    fun loadUrl(context: Context, url: String?, imageView: ImageView?, placeholder: Int? = null) {
        imageView?.run {
            Glide.with(context).clear(this)
            val options = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(placeholder ?: R.drawable.bg_iv_default)
            Glide.with(context)
                .load(url)
                .transition(DrawableTransitionOptions().crossFade())
                .apply(options)
                .into(this)
        }
    }
}

fun ImageView.glideLoader(url: String?, placeholder: Int? = null) {
    Glide.with(context).clear(this)
    val options = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .placeholder(placeholder ?: R.drawable.bg_iv_default)
    Glide.with(context)
        .load(url)
        .transition(DrawableTransitionOptions().crossFade())
        .apply(options)
        .into(this)
}