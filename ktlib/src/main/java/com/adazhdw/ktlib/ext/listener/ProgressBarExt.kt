package com.adazhdw.ktlib.ext.listener

import android.widget.SeekBar

/**
 * author: daguozhu
 * created on: 2019/10/22 19:15
 * description:
 */

fun SeekBar.onProgressChange(action: (progress: Int, fromUser: Boolean) -> Unit) =
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            action.invoke(progress, fromUser)
        }
    })
