package com.adazhdw.kthttp.util


import com.adazhdw.kthttp.KtApp
import com.adazhdw.kthttp.ext.logD
import com.adazhdw.kthttp.ext.logE
import okhttp3.logging.HttpLoggingInterceptor

class OkHttpLogger : HttpLoggingInterceptor.Logger {
    private val TAG = "${KtApp.getApp().packageName}---OkHttpLogger---"
    private val msgBuilder = StringBuilder()
    override fun log(message: String) {
        formatMessage(message)
    }

    private fun formatMessage(message: String) {
        when {
            message.startsWith("--> GET") -> {
                msgBuilder.clear()
                msgBuilder.append("\n \nRequest Type-GET, URL: ${message.replace("--> GET", "")}\n")
            }
            message.startsWith("--> POST") -> {
                msgBuilder.clear()
                msgBuilder.append(
                    "\n \nRequest Type-POST, URL: ${message.replace("--> POST", "")}\n"
                )
            }
            message.startsWith("<-- 200 OK") -> msgBuilder.append(
                "Request Success, URL: ${message.replace("<-- 200 OK", "")}\n"
            )
            message.startsWith("Date:") -> msgBuilder.append("Request Return Time: $message\n")
            message.startsWith("{") -> msgBuilder.append(
                "Response Data: \n" + JsonUtil.formatJson(message) + "\n"
            )
            message.startsWith("<-- END HTTP") -> {
                msgBuilder.append(
                    "Response End---Body Size:${message.replace("<-- END HTTP", "")}\n"
                )
                msgBuilder.toString().logD(TAG)
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
            message.startsWith("<-- 400") -> {//错误请求 — 请求中有语法问题，或不能满足请求。
                msgBuilder.append("Request Error:$message\n")
                ("Request Error:$message\n").logE(TAG)
            }
            message.startsWith("<-- 401") -> {//未授权 — 未授权客户机访问数据
                msgBuilder.append("Request unauthorized:$message\n")
                ("Request unauthorized:$message\n").logE(TAG)
            }
            message.startsWith("<-- 402") -> {//需要付款 — 表示计费系统已有效
                msgBuilder.append("Request Payment required:$message\n")
                ("Request Payment required:$message\n").logE(TAG)
            }
            message.startsWith("<-- 403") -> {//禁止 — 即使有授权也不需要访问
                msgBuilder.append("Request Prohibited:$message\n")
                ("Request Prohibited:$message\n").logE(TAG)
            }
            message.startsWith("<-- 404") -> {//找不到 — 服务器找不到给定的资源；文档不存在,没有这个接口。
                msgBuilder.append("Request Method Error:$message\n")
                ("Request Method Error:$message\n").logE(TAG)
            }
            message.startsWith("<-- 405") -> {//POST或者GET方法使用不对。
                msgBuilder.append("Request Method Type Error:$message\n")
                ("Request Method Type Error:$message\n").logE(TAG)
            }
            message.startsWith("<-- 407") -> {//代理认证请求 — 客户机首先必须使用代理认证自身
                msgBuilder.append("Request Proxy authentication request:$message\n")
                ("Request Proxy authentication request:$message\n").logE(TAG)
            }
            message.startsWith("<-- 415") -> {//介质类型不受支持 — 服务器拒绝服务请求，因为不支持请求实体的格式
                msgBuilder.append("Request Server denial of service request:$message\n")
                ("Request Server denial of service request:$message\n").logE(TAG)
            }
            //5xx 服务器中出现的错误
            message.startsWith("<-- 500") -> {//内部错误 — 因为意外情况，服务器不能完成请求
                msgBuilder.append("Request error:$message\n")
                ("Request error:$message\n").logE(TAG)
            }
            message.startsWith("<-- 501") -> {//未执行 — 服务器不支持请求的工具
                msgBuilder.append("Request error:$message\n")
                ("Request error:$message\n").logE(TAG)
            }
            message.startsWith("<-- 502") -> {//错误网关 — 服务器接收到来自上游服务器的无效响应
                msgBuilder.append("Request error:$message\n")
                ("Request error:$message\n").logE(TAG)
            }
            message.startsWith("<-- 503") -> {//无法获得服务 — 由于临时过载或维护，服务器无法处理请求
                msgBuilder.append("Request error:$message\n")
                ("Request error:$message\n").logE(TAG)
            }
            /*message.startsWith("Server:") -> return
           message.startsWith("Cache-Control:") -> return
           message.startsWith("Set-Cookie:") -> return
           message.startsWith("Expires:") -> return
           message.startsWith("Transfer-Encoding:") -> return
           message.startsWith("Content-Length:") -> return
           message.startsWith("Content-Type:") -> return
           message.startsWith("Content-Language:") -> return
           message.startsWith("--> END GET") -> return
           message.startsWith("--> END POST") -> return*/
            else -> {
                return
            }
        }
    }


}