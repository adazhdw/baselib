package com.grantgz.baseapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NetModelFactory :ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NetViewModel() as T
    }
}