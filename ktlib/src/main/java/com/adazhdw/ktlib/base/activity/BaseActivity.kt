package com.adazhdw.ktlib.base.activity

import android.os.Bundle
import com.adazhdw.ktlib.base.IActivity
import com.adazhdw.ktlib.core.lifecycle.ActivityManager
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity : ForResultActivity(), IActivity {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        ActivityManager.pushActivity(this)

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

    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.popActivity(this)
    }

}

