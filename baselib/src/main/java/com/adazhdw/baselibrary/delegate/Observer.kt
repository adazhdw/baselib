package com.adazhdw.baselibrary.delegate

import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 数据变化监听内联函数
 * @param initializeValue 初始化值
 * @param onValueChange 值变化后调用的方法
 */
inline fun <T> observer(
    initializeValue: T,
    crossinline onValueChange: (oldValue: T, newValue: T) -> Unit
): ReadWriteProperty<Any?, T> =
    object : ObservableProperty<T>(initializeValue) {
        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) =
            onValueChange(oldValue, newValue)
    }