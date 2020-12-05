package com.adazhdw.ktlib.base.mvvm

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get


@MainThread
inline fun <reified VM : ViewModel> FragmentActivity.viewModel(): VM {
    return ViewModelProvider(this).get<VM>()
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.viewModel(): VM {
    return ViewModelProvider(this).get<VM>()
}


















