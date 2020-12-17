package com.adazhdw.libapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adazhdw.ktlib.ext.addFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addFragment(WxChaptersFragment(), R.id.container)
    }
}
