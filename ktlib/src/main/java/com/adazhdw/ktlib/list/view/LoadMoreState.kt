package com.adazhdw.ktlib.list.view


sealed class LoadMoreState {
    object Error : LoadMoreState()
    object Loading : LoadMoreState()
    object Success : LoadMoreState()
}