package com.adazhdw.ktlib.kthttp.converter.parser

import com.adazhdw.ktlib.kthttp.util.ClazzUtil
import com.adazhdw.ktlib.kthttp.util.Preconditions
import com.adazhdw.ktlib.kthttp.util.TypeUtil
import java.lang.reflect.Type

/**
 * author：daguozhu
 * date-time：2020/11/18 11:04
 * description：
 **/
abstract class IParserImpl<T> : IParser<T> {

    @JvmField
    protected val mType: Type

    /**
     * 此构造方法适用于任意Class对象，但更多用于带泛型的Class对象
     */
    constructor() {
        mType = getSuperclassTypeParameter(javaClass, 0)
    }

    /**
     * 此构造方法仅适用于不带泛型的Class对象
     */
    constructor(type: Type) {
        mType = TypeUtil.canonicalize(Preconditions.checkNotNull(type))
    }

    /**
     * 获取当前泛型的type
     */
    private fun getSuperclassTypeParameter(subclass: Class<*>, index: Int): Type {
        return ClazzUtil.getClassType(subclass, index)
    }

}