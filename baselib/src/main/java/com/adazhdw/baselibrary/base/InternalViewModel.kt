package com.adazhdw.baselibrary.base

import com.adazhdw.baselibrary.base.mvvm.BaseRepository
import com.adazhdw.baselibrary.base.mvvm.BaseViewModel

internal class InternalViewModel : BaseViewModel<InternalRepository>() {
    override fun obtainRepository(): InternalRepository {
        return InternalRepository()
    }
}

internal class InternalRepository : BaseRepository()