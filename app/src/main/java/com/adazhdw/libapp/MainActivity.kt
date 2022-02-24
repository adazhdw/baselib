package com.adazhdw.libapp

import com.adazhdw.ktlib.base.activity.ViewBindingActivity
import com.adazhdw.ktlib.ext.addFragment
import com.adazhdw.ktlib.ext.inflate
import com.adazhdw.libapp.databinding.ActivityMainBinding

class MainActivity : ViewBindingActivity() {

    private val binding by inflate<ActivityMainBinding>()

    override fun initView() {
        setContentView(binding.root)
        addFragment(WxChaptersFragment(), R.id.container)
    }

}
