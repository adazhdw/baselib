package com.adazhdw.libapp

import com.adazhdw.ktlib.base.activity.ViewBindingActivity
import com.adazhdw.ktlib.ext.addFragment
import com.adazhdw.libapp.databinding.ActivityMainBinding

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {


    override fun initView() {
        addFragment(WxChaptersFragment(), R.id.container)
    }

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}
