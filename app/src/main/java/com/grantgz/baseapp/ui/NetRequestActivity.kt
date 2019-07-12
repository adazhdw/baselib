package com.grantgz.baseapp.ui

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.adazhdw.baselibrary.base.BaseActivityImpl
import com.adazhdw.baselibrary.http.await
import com.adazhdw.baselibrary.http.subsC
import com.adazhdw.baselibrary.list.BaseRvAdapter
import com.adazhdw.baselibrary.list.BaseViewHolder
import com.adazhdw.baselibrary.list.ListFragmentLine
import com.grantgz.baseapp.InjectorUtil
import com.grantgz.baseapp.R
import com.grantgz.baseapp.http.ChapterHistory
import com.grantgz.baseapp.http.apiService
import kotlinx.android.synthetic.main.net_chapter_item.view.*
import kotlinx.android.synthetic.main.net_request_layout.*
import kotlinx.coroutines.launch

class NetRequestActivity : BaseActivityImpl() {
    override fun layoutId(): Int {
        return R.layout.net_request_layout
    }

    private val mNetViewModel: NetViewModel by lazy {
        ViewModelProviders.of(this, InjectorUtil.getNetModelFactory()).get(NetViewModel::class.java)
    }

    override fun initView() {
        mNetViewModel.mHotKeyList.observe(this, Observer {
            it.forEach {key->
                Log.d(TAG, key.name?:"")
            }
        })
        mNetViewModel.mChapterList.observe(this, Observer {data->
            data?.forEach {
                Log.d(TAG, it.name?:"")
            }
        })
        mNetViewModel.mHistoryData.observe(this, Observer {data->
            data?.datas?.forEach {
                Log.d(TAG, it.title?:"")
            }
        })
        requestBtn.setOnClickListener {
            mNetViewModel.getHotKey()
//            mNetViewModel.getWxArticleChapters2()
//            mNetViewModel.getWxArticleHistory2(408, 1)
        }
        /*supportFragmentManager.beginTransaction()
            .add(R.id.chaptersFl, WxChaptersFragment())
            .commit()*/
    }
}

class WxChaptersFragment : ListFragmentLine<ChapterHistory, BaseViewHolder, ChaptersAdapter>() {
    override fun onNextPage(page: Int, callback: LoadCallback) {
        launch {
            val data = apiService.getWxArticleHistory2(408, page).await()
            callback.onResult()
            callback.onSuccessLoad(data.data?.datas ?: listOf())
        }
    }

    override fun onAdapter(): ChaptersAdapter = ChaptersAdapter(context)
}

class ChaptersAdapter(context: Context?) : BaseRvAdapter<ChapterHistory>(context) {
    override fun onLayoutId(): Int {
        return R.layout.net_chapter_item
    }

    override fun initData(holder: BaseViewHolder, position: Int) {
        super.initData(holder, position)
        holder.itemView.chapterName.text = mData[position].title
    }
}
