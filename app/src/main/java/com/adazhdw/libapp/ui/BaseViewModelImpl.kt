package com.adazhdw.libapp.ui

import com.adazhdw.ktlib.base.mvvm.BaseRepository
import com.adazhdw.ktlib.base.mvvm.BaseViewModel

/**
 * author：daguozhu
 * date-time：2020/11/30 16:42
 * description：
 **/
open class BaseViewModelImpl : BaseViewModel<BaseRepository2>() {
    override fun obtainRepository(): BaseRepository2 {
        return BaseRepository2()
    }
}

class BaseRepository2 : BaseRepository() {}