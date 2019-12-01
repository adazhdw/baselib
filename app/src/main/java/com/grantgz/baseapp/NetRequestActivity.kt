package com.grantgz.baseapp

import android.Manifest
import android.view.View
import android.widget.TextView
import com.adazhdw.ktlib.base.BaseActivityImpl
import com.adazhdw.ktlib.core.delegate.SPDelegate
import com.adazhdw.ktlib.core.network.KtNetCallback
import com.adazhdw.ktlib.ext.addFragment
import com.adazhdw.ktlib.ext.logD
import com.adazhdw.ktlib.ext.toast
import com.adazhdw.ktlib.http.await
import com.adazhdw.ktlib.img.captureImageCoroutines
import com.adazhdw.ktlib.img.selectImageCoroutines
import com.adazhdw.ktlib.list.ListAdapter
import com.adazhdw.ktlib.list.ListFragmentEx
import com.adazhdw.ktlib.list.ListViewHolder
import com.adazhdw.ktlib.mvvm.getViewModel
import com.adazhdw.ktlib.utils.permission.KtPermission
import com.adazhdw.ktlib.utils.permission.PermissionCallback
import com.grantgz.baseapp.http.ChapterHistory
import com.grantgz.baseapp.http.apiService
import kotlinx.android.synthetic.main.net_chapter_item.view.*
import kotlinx.android.synthetic.main.net_request_layout.*
import kotlinx.coroutines.launch

class NetRequestActivity : BaseActivityImpl() {
    override val layoutId: Int
        get() = R.layout.net_request_layout

    private val mNetViewModel: NetViewModel by lazy {
        // @Deprecated---ViewModelProviders.of
//         ViewModelProviders.of(this, InjectorUtil.getNetModelFactory()).get(NetViewModel::class.java)
//         ViewModelProvider(this, InjectorUtil.getNetModelFactory()).get(NetViewModel::class.java)
        // 自己的扩展方法
        getViewModel { NetViewModel() }
    }

    private var isLogin by SPDelegate.preference("isLogin", false)
    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun initView() {

        /* //设置actionbar
         setActionbarCompact(R.id.toolbar)

         spPutValue("", "")
         //login值输出
         ("isLogin-----$isLogin").logD()
         isLogin = true
         ("isLogin-----$isLogin").logD()
         isLogin = false
         ("isLogin-----$isLogin").logD()*/

        requestBtn.setOnClickListener {
            launchOnUI {
                apiService.getHotKey().await()
            }
            /*launch {
                apiService.getHotKey().await()
            }*/
        }
        permissionBtn.setOnClickListener {
            if (!KtPermission.isGranted(permissions, this)) {
                KtPermission.request(
                    this,
                    permissions.toList(),
                    callback = object : PermissionCallback {
                        override fun invoke(p1: Boolean, p2: List<String>) {

                        }
                    })
            } else {
                toast("权限已授予")
            }
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

        addFragment(WxChaptersFragment(), R.id.chaptersFl)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun needNetCallback(): Boolean = true

    override fun onNetAvailable(netType: KtNetCallback.NetType, fromReceiver: Boolean) {
        ("netType----onNetAvailable:$netType---fromReceiver:$fromReceiver").logD(TAG)
    }

    override fun onNetUnAvailable(netType: KtNetCallback.NetType, fromReceiver: Boolean) {
        ("netType----onNetUnAvailable:$netType---fromReceiver:$fromReceiver").logD(TAG)
    }

}

class WxChaptersFragment : ListFragmentEx<ChapterHistory, ChaptersAdapter>() {

    override val mLoadMoreEnable: Boolean
        get() = true

    override fun starAtPage(): Int {
        return 0
    }

    override fun nextPage(page: Int, callback: OnRequestCallback<ChapterHistory>) {
        launch {
            val data = apiService.getWxArticleHistory2(408, page).await()
            mHandler.postDelayed({
                callback.onSuccess(data.data?.datas ?: listOf())
            },500)
        }
    }

    override fun onAdapter(): ChaptersAdapter = ChaptersAdapter()
}

class ChaptersAdapter : ListAdapter() {
    override fun layoutId(): Int {
        return R.layout.net_chapter_item
    }

    override fun bindHolder(holder: ListViewHolder, data: Any, position: Int) {
        holder.itemView.chapterName.text = (data as ChapterHistory).title
    }
}
