package com.adazhdw.ktlib.kthttp.model;

import androidx.annotation.NonNull;

import com.adazhdw.ktlib.KtLib;
import com.adazhdw.ktlib.kthttp.coder.ICoder;
import com.adazhdw.ktlib.kthttp.coder.UrlCoder;
import com.adazhdw.ktlib.kthttp.converter.GsonConverter;
import com.adazhdw.ktlib.kthttp.converter.IConverter;
import com.adazhdw.ktlib.kthttp.interceptor.RetryInterceptor;
import com.adazhdw.ktlib.kthttp.ssl.HttpsUtils;
import com.adazhdw.ktlib.kthttp.util.OkHttpLogger;
import com.adazhdw.ktlib.kthttp.util.logging.Level;
import com.adazhdw.ktlib.kthttp.util.logging.LoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * author：daguozhu
 * date-time：2020/11/9 9:44
 * description：
 **/
public class KtConfig {

    private static ICoder coder = UrlCoder.Companion.create();
    private static IConverter converter = GsonConverter.Companion.create();
    private static boolean needDecodeResult = false;
    private static OkHttpClient mOkHttpClient = getOkHttpClient(HttpConstant.DEFAULT_TIMEOUT);
    private static boolean debug = true;

    public static IConverter getConverter() {
        return converter;
    }

    public static void setConverter(@NonNull IConverter converter) {
        KtConfig.converter = converter;
    }

    public static ICoder getCoder() {
        return coder;
    }

    public static void setCoder(ICoder coder) {
        KtConfig.coder = coder;
    }

    public static boolean isNeedDecodeResult() {
        return needDecodeResult;
    }

    public static void setNeedDecodeResult(boolean needDecodeResult) {
        KtConfig.needDecodeResult = needDecodeResult;
    }

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static void setOkHttpClient(OkHttpClient mOkHttpClient) {
        KtConfig.mOkHttpClient = mOkHttpClient;
    }

    public static OkHttpClient getOkHttpClient(Long timeout) {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        return new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .callTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(getLoggingInterceptor2())
                .addInterceptor(new RetryInterceptor())
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager).build();
    }

    private static Interceptor getLoggingInterceptor2() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new OkHttpLogger());
        if (KtLib.INSTANCE.isDebug$ktlib_debug()) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        return interceptor;
    }

    public static Interceptor getLoggingInterceptor() {
        Level level;
        if (KtLib.INSTANCE.isDebug$ktlib_debug()) {
            level = Level.BODY;
        } else {
            level = Level.BASIC;
        }
        return new LoggingInterceptor.Builder()
                .setLevel(level).build();
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        KtConfig.debug = debug;
    }
}
