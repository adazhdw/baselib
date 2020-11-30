package com.adazhdw.libapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adazhdw.ktlib.kthttp.KtHttp
import com.adazhdw.ktlib.kthttp.coroutines.toClazz
import com.adazhdw.ktlib.kthttp.entity.Param
import com.adazhdw.libapp.bean.DataFeed
import com.adazhdw.libapp.bean.NetResponse
import com.adazhdw.libapp.ui.BaseViewModelImpl

class HomeViewModel : BaseViewModelImpl() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getText() {
        launch {
            val data = KtHttp.ktHttp.get(
                url = "https://wanandroid.com/wxarticle/list/408/1/json",
                param = Param.build().addParam("k", "Android")
            ).toClazz<NetResponse<DataFeed>>().await()
            val stringBuilder = StringBuilder()
            for (item in data.data.datas) {
                stringBuilder.append("标题：${item.title}").append("\n\n")
            }
            _text.postValue(stringBuilder.toString())
        }
    }
}