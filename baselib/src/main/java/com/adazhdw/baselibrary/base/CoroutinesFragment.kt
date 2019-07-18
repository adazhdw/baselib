package com.adazhdw.baselibrary.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class CoroutinesFragment : Fragment(), CoroutineScope {

    private val myViewModel = InternalViewModel()
    override val coroutineContext: CoroutineContext
        get() = myViewModel.viewModelScope.coroutineContext


    override fun onDestroyView() {
        super.onDestroyView()
        myViewModel.viewModelScope.cancel()
    }
}