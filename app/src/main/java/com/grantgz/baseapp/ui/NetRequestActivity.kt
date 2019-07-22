package com.grantgz.baseapp.ui

import android.Manifest
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.adazhdw.baselibrary.base.BaseActivityImpl
import com.adazhdw.baselibrary.ext.*
import com.adazhdw.baselibrary.http.await
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

    private val URL = "https://static.usasishu.com/com.uuabc.samakenglish_5.1.12_35.apk"
    private var isLogin by DelegateExt.preference("isLogin", false)
    private val permissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun initView() {
        /*logD("isLogin-----$isLogin")
        isLogin = true
        logD("isLogin-----$isLogin")
        isLogin = false
        logD("isLogin-----$isLogin")*/

        mNetViewModel.mHotKeyList.observe(this, Observer {
            it.forEach { key ->
                Log.d(TAG, key.name ?: "")
            }
        })
        mNetViewModel.mChapterList.observe(this, Observer { data ->
            data?.forEach {
                Log.d(TAG, it.name ?: "")
            }
        })
        mNetViewModel.mHistoryData.observe(this, Observer { data ->
            data?.datas?.forEach {
                Log.d(TAG, it.title ?: "")
            }
        })
        requestBtn.setOnClickListener {
            mNetViewModel.getHotKey()
            mNetViewModel.getWxArticleChapters2()
            mNetViewModel.getWxArticleHistory2(408, 1)
            /*launch {
                arrayOf(
                    async {
                        apiService.getWxArticleChapters2().await()
                    },
                    async {
                        apiService.getHotKey().await()
                    },
                    async {
                        apiService.getWxArticleHistory2(428, 1).await()
                    }).forEach { it.await() }
            }*/
        }
        permissionBtn.setOnClickListener {
            if (!PermissionExt.isGranted(permissions, this)) {
                PermissionExt.requestPermissions(this, permissions,
                    granted = {
                        it.forEach { permission ->
                            logD(TAG, "onGranted----$permission")
                        }
                    }, denied = {
                        it.forEach { permission ->
                            logD(TAG, "onDenied----$permission")
                        }
                    })
            } else {
                toast("权限已授予")
            }
        }

        downloadBtn.setOnClickListener {
            downloadFile(URL)
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.chaptersFl, WxChaptersFragment())
            .commit()
    }
}

class WxChaptersFragment : ListFragmentLine<ChapterHistory, BaseViewHolder, ChaptersAdapter>() {

    override fun onNextPage(page: Int, callback: LoadCallback) {
        launch {
            val data = apiService.getWxArticleHistory2(428, page).await()
            callback.onResult()
            callback.onSuccessLoad(data.data?.datas ?: listOf())
        }
    }

    override fun onAdapter(): ChaptersAdapter = ChaptersAdapter()
}

class ChaptersAdapter : BaseRvAdapter<ChapterHistory>() {
    override fun onLayoutId(): Int {
        return R.layout.net_chapter_item
    }

    override fun onFooterLayoutId(): Int {
        return R.layout.list_base_footer_impl
    }

    override fun initData(holder: BaseViewHolder, position: Int) {
        super.initData(holder, position)
        holder.itemView.chapterName.text = mData[position].title
    }
}
