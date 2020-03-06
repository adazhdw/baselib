package com.adazhdw.ktlib.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity : ForResultActivity(), IActivity {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        window.setBackgroundDrawable(null)
        initView()
        initData()
        requestStart()
    }

    override fun onStart() {
        super.onStart()
        if (needEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStop() {
        super.onStop()
        if (needEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

}

