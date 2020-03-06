package com.adazhdw.ktlib.core

import android.content.Context
import com.adazhdw.ktlib.LibUtil
import java.lang.IllegalArgumentException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> preference(paramName: String, default: T, spName: String = LibUtil.getApp().packageName + "_sharePreference"): Preference<T> =
    Preference(spName, paramName, default)

class Preference<T>(private val spName: String, private val paramName: String, private val default: T) : ReadWriteProperty<Any, T> {

    private val pref by lazy { LibUtil.getApp().applicationContext.getSharedPreferences(spName, Context.MODE_PRIVATE) }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return getParam(paramName, default)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        putParam(paramName, value)
    }

    private fun putParam(paramName: String, value: T) = with(pref.edit()) {
        when (value) {
            is Long -> putLong(paramName, value)
            is Boolean -> putBoolean(paramName, value)
            is Int -> putInt(paramName, value)
            is String -> putString(paramName, value)
            is Float -> putFloat(paramName, value)
            else -> throw IllegalArgumentException("This type can't be saved into Preferences")
        }
    }.apply()

    @Suppress("UNCHECKED_CAST")
    private fun <T> getParam(paramName: String, default: T): T = with(pref) {
        return when (default) {
            is Long -> getLong(paramName, 0L) as T
            is Boolean -> getBoolean(paramName, false) as T
            is Int -> getInt(paramName, 0) as T
            is String -> getString(paramName, "") as T
            is Float -> getFloat(paramName, 0f) as T
            else -> throw IllegalArgumentException("This type can't be saved into Preferences")
        }
    }
}