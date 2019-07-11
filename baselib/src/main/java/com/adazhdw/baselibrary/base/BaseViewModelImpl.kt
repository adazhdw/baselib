package com.adazhdw.baselibrary.base

import com.adazhdw.baselibrary.base.mvvm.BaseRepository
import com.adazhdw.baselibrary.base.mvvm.BaseViewModel

internal class BaseViewModelImpl :BaseViewModel<BaseRepositoryImpl>() {
    override fun obtainRepository(): BaseRepositoryImpl {
        return BaseRepositoryImpl()
    }
}

internal class BaseRepositoryImpl :BaseRepository()