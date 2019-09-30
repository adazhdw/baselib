package com.grantgz.baseapp

import android.Manifest
import com.adazhdw.baselibrary.base.BaseActivityImpl
import com.adazhdw.baselibrary.delegate.DelegateExt
import com.adazhdw.baselibrary.ext.*
import com.adazhdw.baselibrary.http.await
import com.adazhdw.baselibrary.img.captureImageCoroutines
import com.adazhdw.baselibrary.img.selectImageCoroutines
import com.adazhdw.baselibrary.list.BaseRvAdapter
import com.adazhdw.baselibrary.list.BaseViewHolder
import com.adazhdw.baselibrary.list.ListFragmentLine
import com.adazhdw.baselibrary.utils.PermissionUtil
import com.grantgz.baseapp.ext.downloadFile
import com.grantgz.baseapp.http.ChapterHistory
import com.grantgz.baseapp.http.apiService
import kotlinx.android.synthetic.main.net_chapter_item.view.*
import kotlinx.android.synthetic.main.net_request_layout.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NetRequestActivity : BaseActivityImpl() {
    override val layoutId: Int
        get() = R.layout.net_request_layout

    private val mNetViewModel: NetViewModel by lazy {
        // @Deprecated---ViewModelProviders.of
        // ViewModelProviders.of(this, InjectorUtil.getNetModelFactory()).get(NetViewModel::class.java)
        // ViewModelProvider(this, InjectorUtil.getNetModelFactory()).get(NetViewModel::class.java)
        // 自己的扩展方法
        getViewModel { NetViewModel() }
    }

    private val downloadUrl = "https://static.usasishu.com/com.uuabc.samakenglish_5.1.12_35.apk"
    private var isLogin by DelegateExt.preference("isLogin", false)
    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun initView() {

        //设置actionbar
        setActionbarCompact(R.id.toolbar)

        //login值输出
        logD("isLogin-----$isLogin")
        isLogin = true
        logD("isLogin-----$isLogin")
        isLogin = false
        logD("isLogin-----$isLogin")

        requestBtn.setOnClickListener {
            launch {
                arrayOf(
                    async {
                        apiService.getHotKey().await()
                    },
                    async {
                        apiService.getWxArticleChapters2().await()
                    }).forEach { it.await() }
            }
        }
        permissionBtn.setOnClickListener {
            if (!PermissionUtil.isGranted(permissions, this)) {
                PermissionUtil.requestPermissions(this, permissions,
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
            downloadFile(downloadUrl)
        }
        selectImgBtn.setOnClickListener {
            launch {
                val model = selectImageCoroutines()
                selectImg.setImageURI(model.uri)
            }
        }
        captureImgBtn.setOnClickListener {
            launch {
                val model = captureImageCoroutines()
                selectImg.setImageURI(model.uri)
            }
        }

        replaceFragment(WxChaptersFragment(),R.id.chaptersFl)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
