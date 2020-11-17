package com.adazhdw.libapp.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.adazhdw.ktlib.base.mvvm.viewModel
import com.adazhdw.ktlib.kthttp.ext.postRequest
import com.adazhdw.ktlib.kthttp.model.Param
import com.adazhdw.libapp.R

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel = viewModel<NotificationsViewModel>()
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView: TextView = view.findViewById(R.id.text_notifications)
        textView.setOnClickListener {
            postRequest<String>(
                url = "https://www.wanandroid.com/article/query/0/json",
                param = Param.build().addParam("k", "ViewModel"),
                success = { data ->
                    textView.text = data.parseAsHtml()
                }, error = { code, msg ->
                    textView.text = ("code:$code,msg:$msg")
                }
            )
        }
    }
}
