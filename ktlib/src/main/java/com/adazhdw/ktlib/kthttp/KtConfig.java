package com.adazhdw.ktlib.kthttp;

import com.adazhdw.ktlib.kthttp.converter.GsonConverter;
import com.adazhdw.ktlib.kthttp.converter.IConverter;

/**
 * author：daguozhu
 * date-time：2020/11/3 19:38
 * description：
 **/

public class KtConfig {
    private static IConverter converter = GsonConverter.Companion.create();

    public static IConverter getConverter() {
        return converter;
    }

    public static void setConverter(IConverter converter) {
        KtConfig.converter = converter;
    }
}
