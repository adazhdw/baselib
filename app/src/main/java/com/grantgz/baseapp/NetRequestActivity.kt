package com.grantgz.baseapp

import android.Manifest
import android.content.Context
import com.adazhdw.ktlib.base.BaseActivityImpl
import com.adazhdw.ktlib.base.mvvm.getViewModel
import com.adazhdw.ktlib.core.delegate.SPDelegate
import com.adazhdw.ktlib.core.network.KtNetCallback
import com.adazhdw.ktlib.ext.logD
import com.adazhdw.ktlib.ext.toast
import com.adazhdw.ktlib.ext.view.invisible
import com.adazhdw.ktlib.http.await
import com.adazhdw.ktlib.img.captureImageCoroutines
import com.adazhdw.ktlib.img.selectImageCoroutines
import com.adazhdw.ktlib.list.ListAdapter
import com.adazhdw.ktlib.list.ListFragmentEx
import com.adazhdw.ktlib.list.ListViewHolder
import com.adazhdw.ktlib.utils.permission.KtPermission
import com.adazhdw.ktlib.utils.permission.PermissionCallback
import com.grantgz.baseapp.http.ChapterHistory
import com.grantgz.baseapp.http.HotKey
import com.grantgz.baseapp.http.ListResponse
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
        selectImgBtn.invisible()
        captureImgBtn.invisible()
        selectImgBtn.setOnClickListener {
            launchOnUI {
                val model = selectImageCoroutines()
                selectImg.setImageURI(model.uri)
            }
        }
        captureImgBtn.setOnClickListener {
            launchOnUI {
                val model = captureImageCoroutines()
                selectImg.setImageURI(model.uri)
            }
        }

//        addFragment(WxChaptersFragment(), R.id.chaptersFl)
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
        get() = false

    override fun starAtPage(): Int {
        return 1
    }

    override fun nextPage(page: Int, callback: OnRequestCallback<ChapterHistory>) {
        try {
            launchOnUI {
                val data = apiService.getWxArticleHistory2(428, page).await().data
                mHandler.postDelayed({
                    if (data != null)
                        callback.onSuccess(data.datas ?: listOf(), total = data.total)
                    else
                        callback.onError(0, "数据为空")
                }, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAdapter(): ChaptersAdapter = ChaptersAdapter(mContext)
}

class ChaptersAdapter(context: Context) : ListAdapter(context) {

    override val layoutId: Int
        get() = R.layout.net_chapter_item

    override fun bindHolder(holder: ListViewHolder, data: Any, position: Int) {
        holder.itemView.chapterName.text = (data as ChapterHistory).title
    }
}
