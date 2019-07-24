package com.adazhdw.baselibrary.ext

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.adazhdw.baselibrary.LibUtil


fun Fragment.showMsg(msg: String?) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.startAcvitity(clazz: Class<*>) {
    startActivity(Intent(activity, clazz))
}

fun FragmentActivity.startAcvitity(clazz: Class<*>) {
    startActivity(Intent(this, clazz))
}

fun FragmentActivity.showMsg(msg: String?) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun toast(msg: String?) {
    Toast.makeText(LibUtil.getApp(), msg, Toast.LENGTH_SHORT).show()
}
