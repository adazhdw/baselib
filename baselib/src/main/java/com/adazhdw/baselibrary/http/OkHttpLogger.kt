package com.adazhdw.baselibrary.http

import com.adazhdw.baselibrary.ext.logD
import com.adazhdw.baselibrary.ext.logE
import com.adazhdw.baselibrary.ext.startWidth
import com.adazhdw.baselibrary.utils.JsonUtils
import okhttp3.logging.HttpLoggingInterceptor

class OkHttpLogger : HttpLoggingInterceptor.Logger {
    private val TAG = "OkHttpLogger------"
    private val msgBuilder = StringBuilder()
    override fun log(message: String) {
        formatMessage(message)
    }

    private fun formatMessage(message: String) {
        when {
            /*message.startWidth("Server:") -> return
            message.startWidth("Cache-Control:") -> return
            message.startWidth("Set-Cookie:") -> return
            message.startWidth("Expires:") -> return
            message.startWidth("Transfer-Encoding:") -> return
            message.startWidth("Content-Length:") -> return
            message.startWidth("Content-Type:") -> return
            message.startWidth("Content-Language:") -> return
            message.startWidth("--> END GET") -> return
            message.startWidth("--> END POST") -> return*/
            message.startWidth("<-- 405 Method Not Allowed") -> {
                msgBuilder.append("Request Method Type Error:$message\n")
                logE("Request Method Type Error:$message\n")
            }
            message.startWidth("<-- 404 Not Found") -> {
                msgBuilder.append("Request Method Error:$message\n")
                logE("Request Method Error:$message\n")
            }
            message.startWidth("--> GET") -> {
                msgBuilder.clear()
                msgBuilder.append("\n \nRequest Type-GET,URL: ${message.replace("--> GET", "")}\n")
            }
            message.startWidth("--> POST") -> {
                msgBuilder.clear()
                msgBuilder.append("\n \nRequest Type-POST,URL: ${message.replace("--> POST", "")}\n")
            }
            message.startWidth("<-- 200 OK") -> msgBuilder.append("Request Success,URL: ${message.replace("<-- 200 OK", "")}\n")
            message.startWidth("Date:") -> msgBuilder.append("Request Return Time: $message\n")
            message.startWidth("{")->msgBuilder.append("Response Data：\n"+JsonUtils.formatJson(message)+"\n")
            message.startWidth("<-- END HTTP") -> {
                msgBuilder.append("Response End---Body Size:${message.replace("<-- END HTTP", "")}\n")
                logD(TAG,msgBuilder.toString())
            }
            else -> return
        }
    }


}