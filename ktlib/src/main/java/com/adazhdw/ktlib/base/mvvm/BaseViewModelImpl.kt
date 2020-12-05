package com.adazhdw.ktlib.base.mvvm

/**
 * author：daguozhu
 * date-time：2020/12/2 15:40
 * description：
 **/
open class BaseViewModelImpl : BaseViewModel<BaseRepository>() {
    override fun obtainRepository(): BaseRepository = BaseRepository()

    protected suspend fun <T> launchOnIO(block: suspend () -> T): T {
        return mRepository.launchOnIO(block)
    }
}