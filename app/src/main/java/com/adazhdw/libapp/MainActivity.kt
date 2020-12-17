package com.adazhdw.libapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adazhdw.ktlib.ext.addFragment
import com.adazhdw.libapp.notifications.NotificationsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addFragment(NotificationsFragment(), R.id.container)
    }
}
