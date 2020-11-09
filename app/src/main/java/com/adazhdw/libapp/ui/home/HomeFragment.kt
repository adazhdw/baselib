package com.adazhdw.libapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.parseAsHtml
import androidx.lifecycle.Observer
import com.adazhdw.ktlib.base.fragment.BaseFragment
import com.adazhdw.ktlib.base.mvvm.viewModel
import com.adazhdw.ktlib.ext.logD
import com.adazhdw.ktlib.kthttp.ext.getCoroutines
import com.adazhdw.ktlib.kthttp.model.Params
import com.adazhdw.libapp.R
import com.adazhdw.libapp.bean.DataFeed
import com.adazhdw.libapp.bean.NetResponse
import kotlin.system.measureTimeMillis

class HomeFragment : BaseFragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = viewModel<HomeViewModel>()
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView: TextView = view.findViewById(R.id.text_home)

        launch {
            /*val data = requestCoroutines<NetResponse<DataFeed>>(
                url = "https://wanandroid.com/wxarticle/list/408/1/json",
                params = KParams.Builder().addParams(mapOf("k" to "Android")).build()
            )*/
            val time = measureTimeMillis {
                val data = getCoroutines<NetResponse<DataFeed>>(
                    url = "https://wanandroid.com/wxarticle/list/408/1/json",
                    params = Params.Builder().addParam("k", "Android").build()
                )
                val stringBuilder = StringBuilder()
                for (item in data.data.datas) {
                    stringBuilder.append("标题：${item.title}".parseAsHtml()).append("\n\n")
                }
                textView.text = stringBuilder.toString()
            }
            "$time".logD(TAG)
        }

        /*getRequest<NetResponse<DataFeed>>(
            url = "https://wanandroid.com/wxarticle/list/408/1/json",
            params = KParams.Builder().addParams(mapOf("k" to "Android")).build(),
            success = {
                textView.text = it.toString()
            }, error = { code, msg ->
                "code:$code,msg:$msg-error-happened".logD("HomeFragment")
            })*/
    }

    override val layoutId: Int
        get() = R.layout.fragment_home
}
