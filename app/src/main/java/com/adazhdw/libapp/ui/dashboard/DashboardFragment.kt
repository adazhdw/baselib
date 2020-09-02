package com.adazhdw.libapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.adazhdw.ktlib.base.fragment.BaseFragment
import com.adazhdw.ktlib.kthttp.param.Param
import com.adazhdw.ktlib.kthttp.postCoroutines
import com.adazhdw.libapp.R
import com.adazhdw.libapp.bean.DataFeed
import com.adazhdw.libapp.bean.NetResponse

class DashboardFragment(override val layoutId: Int = R.layout.fragment_dashboard) : BaseFragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
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

        launch {
            /*val data = requestCoroutines<NetResponse<DataFeed>>(
                method = POST,
                url = "https://www.wanandroid.com/article/query/0/json",
                params = KParams.Builder().addHeaders(mapOf("k" to "ViewModel")).build()
            )*/
            val data = postCoroutines<NetResponse<DataFeed>>(
                url = "https://www.wanandroid.com/article/query/0/json",
                param = Param.Builder().addParams(mapOf("k" to "ViewModel")).build()
            )
            textView.text = data.toString()
        }

        /*postRequest<NetResponse<DataFeed>>(
            url = "https://www.wanandroid.com/article/query/0/json",
            params = KParams.Builder().addHeaders(mapOf("k" to "ViewModel")).build(),
            success = {
                textView.text = it.toString()
            }, error = { code, msg ->

            }
        )*/
    }
}
