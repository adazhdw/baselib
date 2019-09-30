package com.adazhdw.baselibrary.http

import com.adazhdw.baselibrary.ext.logD
import com.adazhdw.baselibrary.ext.logE
import com.adazhdw.baselibrary.ext.startWidth
import okhttp3.logging.HttpLoggingInterceptor

class OkHttpLogger : HttpLoggingInterceptor.Logger {
    private val TAG = "OkHttpLogger------"
    private val msgBuilder = StringBuilder()
    override fun log(message: String) {
        formatMessage(message)
    }

    private fun formatMessage(message: String) {
        when {
            message.startWidth("--> GET") -> {
                msgBuilder.clear()
                msgBuilder.append("\n \nRequest Type-GET, URL: ${message.replace("--> GET", "")}\n")
            }
            message.startWidth("--> POST") -> {
                msgBuilder.clear()
                msgBuilder.append(
                    "\n \nRequest Type-POST, URL: ${message.replace(
                        "--> POST",
                        ""
                    )}\n"
                )
            }
            message.startWidth("<-- 200 OK") -> msgBuilder.append(
                "Request Success, URL: ${message.replace(
                    "<-- 200 OK",
                    ""
                )}\n"
            )
            message.startWidth("Date:") -> msgBuilder.append("Request Return Time: $message\n")
            message.startWidth("{") -> msgBuilder.append(
                "Response Data: \n" + JsonUtil.formatJson(
                    message
                ) + "\n"
            )
            message.startWidth("<-- END HTTP") -> {
                msgBuilder.append(
                    "Response End---Body Size:${message.replace(
                        "<-- END HTTP",
                        ""
                    )}\n"
                )
                logD(TAG, msgBuilder.toString())
            }
            else -> errorHandle(message)
        }
    }

    /**
     * 错误信息统一处理
     */
    private fun errorHandle(message: String) {
        when {
            //4xx 客户机中出现的错误
            message.startWidth("<-- 400") -> {//错误请求 — 请求中有语法问题，或不能满足请求。
                msgBuilder.append("Request Error:$message\n")
                logE("Request Error:$message\n")
            }
            message.startWidth("<-- 401") -> {//未授权 — 未授权客户机访问数据
                msgBuilder.append("Request unauthorized:$message\n")
                logE("Request unauthorized:$message\n")
            }
            message.startWidth("<-- 402") -> {//需要付款 — 表示计费系统已有效
                msgBuilder.append("Request Payment required:$message\n")
                logE("Request Payment required:$message\n")
            }
            message.startWidth("<-- 403") -> {//禁止 — 即使有授权也不需要访问
                msgBuilder.append("Request Prohibited:$message\n")
                logE("Request Prohibited:$message\n")
            }
            message.startWidth("<-- 404") -> {//找不到 — 服务器找不到给定的资源；文档不存在,没有这个接口。
                msgBuilder.append("Request Method Error:$message\n")
                logE("Request Method Error:$message\n")
            }
            message.startWidth("<-- 405") -> {//POST或者GET方法使用不对。
                msgBuilder.append("Request Method Type Error:$message\n")
                logE("Request Method Type Error:$message\n")
            }
            message.startWidth("<-- 407") -> {//代理认证请求 — 客户机首先必须使用代理认证自身
                msgBuilder.append("Request Proxy authentication request:$message\n")
                logE("Request Proxy authentication request:$message\n")
            }
            message.startWidth("<-- 415") -> {//介质类型不受支持 — 服务器拒绝服务请求，因为不支持请求实体的格式
                msgBuilder.append("Request Server denial of service request:$message\n")
                logE("Request Server denial of service request:$message\n")
            }
            //5xx 服务器中出现的错误
            message.startWidth("<-- 500") -> {//内部错误 — 因为意外情况，服务器不能完成请求
                msgBuilder.append("Request error:$message\n")
                logE("Request error:$message\n")
            }
            message.startWidth("<-- 501") -> {//未执行 — 服务器不支持请求的工具
                msgBuilder.append("Request error:$message\n")
                logE("Request error:$message\n")
            }
            message.startWidth("<-- 502") -> {//错误网关 — 服务器接收到来自上游服务器的无效响应
                msgBuilder.append("Request error:$message\n")
                logE("Request error:$message\n")
            }
            message.startWidth("<-- 503") -> {//无法获得服务 — 由于临时过载或维护，服务器无法处理请求
                msgBuilder.append("Request error:$message\n")
                logE("Request error:$message\n")
            }
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
            else -> {
                return
            }
        }
    }


}