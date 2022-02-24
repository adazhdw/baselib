package com.adazhdw.libapp

import com.adazhdw.ktlib.base.activity.ViewBindingActivity
import com.adazhdw.ktlib.ext.addFragment
import com.adazhdw.ktlib.ext.viewBind
import com.adazhdw.libapp.databinding.ActivityMainBinding

class MainActivity : ViewBindingActivity() {

    private val binding by viewBind<ActivityMainBinding>()

    override fun initView() {
        setContentView(binding.root)
        addFragment(WxChaptersFragment(), R.id.container)
    }

}
