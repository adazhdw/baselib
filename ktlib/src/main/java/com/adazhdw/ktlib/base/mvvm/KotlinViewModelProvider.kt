package com.adazhdw.ktlib.base.mvvm

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

class KotlinViewModelProvider private constructor() {

    companion object {
        inline fun <reified VM : ViewModel> of(fragment: Fragment, crossinline factory: () -> VM): VM {

            @Suppress("UNCHECKED_CAST")
            val vmFactory = object : ViewModelProvider.NewInstanceFactory() {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T = factory() as T
            }
            return ViewModelProvider(fragment, vmFactory).get(VM::class.java)
        }

        inline fun <reified VM : ViewModel> of(activity: FragmentActivity, crossinline factory: () -> VM): VM {

            @Suppress("UNCHECKED_CAST")
            val vmFactory = object : ViewModelProvider.NewInstanceFactory() {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T = factory() as T
            }
            return ViewModelProvider(activity, vmFactory).get(VM::class.java)
        }
    }

}


@MainThread
inline fun <reified T : ViewModel> Fragment.getViewModel(crossinline factory: () -> T): T {
    return KotlinViewModelProvider.of(this, factory)
}

@MainThread
inline fun <reified T : ViewModel> FragmentActivity.getViewModel(crossinline factory: () -> T): T {
    return KotlinViewModelProvider.of(this, factory)
}
