package com.adazhdw.libapp

import com.adazhdw.ktlib.base.activity.ViewBindingActivity
import com.adazhdw.ktlib.core.viewbinding.inflate
import com.adazhdw.ktlib.ext.addFragment
import com.adazhdw.libapp.databinding.ActivityMainBinding

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {

    override val viewBinding by inflate<ActivityMainBinding>()

    override fun initView() {
        addFragment(WxChaptersFragment(), R.id.container)
    }

}
