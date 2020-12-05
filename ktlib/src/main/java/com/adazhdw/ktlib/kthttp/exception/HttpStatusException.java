package com.adazhdw.ktlib.kthttp.exception;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Http 状态码 小于200或者大于等于300时,或者ResponseBody等于null，抛出此异常
 */
public final class HttpStatusException extends IOException {

    private final int statusCode; //Http响应状态吗
    private final String result;    //返回结果
    private final String requestMethod; //请求方法，Get/Post等
    private final String requestUrl; //请求Url及参数
    private final Headers responseHeaders; //响应头

    public HttpStatusException(Response response) {
        this(response, null);
    }

    public HttpStatusException(Response response, String result) {
        super(response.message());
        statusCode = response.code();
        Request request = response.request();
        requestMethod = request.method();
        requestUrl = request.url().toString();
        responseHeaders = response.headers();
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public Headers getResponseHeaders() {
        return responseHeaders;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + ":" +
                " Method=" + requestMethod +
                " Code=" + statusCode +
                "\nmessage = " + getMessage() +
                "\n\n" + requestUrl +
                "\n\n" + responseHeaders +
                "\n" + result;
    }
}
