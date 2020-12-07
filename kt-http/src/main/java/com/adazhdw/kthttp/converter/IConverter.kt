package com.adazhdw.kthttp.converter

import java.lang.reflect.Type

/**
 * author：daguozhu
 * date-time：2020/9/5 16:13
 * description：
 **/
interface IConverter {
    /**
     * 拿到响应后，将数据转换成需要的格式，子线程中执行，可以是耗时操作
     *
     * @param result 需要转换的对象
     * @return 转换后的结果
     * @throws Exception 转换过程发生的异常
     */
    @Throws(Throwable::class)
    fun <T> convert(result: String, type: Type?, needDecodeResult: Boolean): T
}