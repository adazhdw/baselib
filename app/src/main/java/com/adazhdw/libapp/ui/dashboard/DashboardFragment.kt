package com.adazhdw.libapp.ui.dashboard

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
import com.adazhdw.ktlib.kthttp.ext.postCoroutines
import com.adazhdw.ktlib.kthttp.model.Params
import com.adazhdw.libapp.R
import com.adazhdw.libapp.bean.DataFeed
import com.adazhdw.libapp.bean.NetResponse
import kotlin.system.measureTimeMillis

class DashboardFragment(override val layoutId: Int = R.layout.fragment_dashboard) : BaseFragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel = viewModel<DashboardViewModel>()
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView: TextView = view.findViewById(R.id.text_dashboard)

        launchOnUI {
            /*val data = requestCoroutines<NetResponse<DataFeed>>(
                method = POST,
                url = "https://www.wanandroid.com/article/query/0/json",
                params = KParams.Builder().addParams(mapOf("k" to "ViewModel")).build()
            )*/
            val time = measureTimeMillis {
                val data = postCoroutines<NetResponse<DataFeed>>(
                    url = "https://www.wanandroid.com/article/query/0/json",
                    params = Params.Builder().addParam("k", "ViewModel").build()
                )

                val stringBuilder = StringBuilder()
                for (item in data.data.datas) {
                    stringBuilder.append("标题：${item.title}".parseAsHtml()).append("\n\n")
                }
                textView.text = stringBuilder.toString()
            }
            "$time".logD(TAG)
        }

        /*postRequest<NetResponse<DataFeed>>(
            url = "https://www.wanandroid.com/article/query/0/json",
            params = KParams.Builder().addParams(mapOf("k" to "ViewModel")).build(),
            success = {
                textView.text = it.toString()
            }, error = { code, msg ->

            }
        )*/
    }
}
