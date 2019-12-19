package com.adazhdw.ktlib.list


interface LoadMoreView {

    /**
     * Show progress.
     */
    fun onLoading()

    /**
     * Load finish, handle result.
     */
    fun onLoadFinish(dataEmpty: Boolean, hasMore: Boolean)

    /**
     * Non-auto-loading mode, you can to click on the item to load.
     */
    fun onWaitToLoadMore(loadMoreListener: ListRecyclerView.LoadMoreListener)

    /**
     * Load error.
     */
    fun onLoadError(errorCode: Int, errorMsg: String?)
}
