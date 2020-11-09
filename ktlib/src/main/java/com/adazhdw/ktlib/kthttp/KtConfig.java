package com.adazhdw.ktlib.kthttp;

import androidx.annotation.NonNull;

import com.adazhdw.ktlib.kthttp.coder.ICoder;
import com.adazhdw.ktlib.kthttp.coder.UrlCoder;
import com.adazhdw.ktlib.kthttp.converter.GsonConverter;
import com.adazhdw.ktlib.kthttp.converter.IConverter;

/**
 * author：daguozhu
 * date-time：2020/11/9 9:44
 * description：
 **/
public class KtConfig {

    private static ICoder coder = UrlCoder.Companion.create();
    private static IConverter converter = GsonConverter.Companion.create();
    private static boolean needDecodeResult = false;

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
}
