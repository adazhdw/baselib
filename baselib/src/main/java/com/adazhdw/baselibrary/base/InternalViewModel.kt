package com.adazhdw.baselibrary.base

import com.adazhdw.baselibrary.mvvm.BaseRepository
import com.adazhdw.baselibrary.mvvm.BaseViewModel

internal class InternalViewModel : BaseViewModel<InternalRepository>() {
    override fun obtainRepository(): InternalRepository {
        return InternalRepository()
    }
}

internal class InternalRepository : BaseRepository()