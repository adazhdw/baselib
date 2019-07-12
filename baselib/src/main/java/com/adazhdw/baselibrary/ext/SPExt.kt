package com.adazhdw.baselibrary.ext

import android.content.Context
import com.adazhdw.baselibrary.LibUtil
import java.lang.IllegalArgumentException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Preference<T>(private val spName: String, private val paramName: String, private val default: T) :
    ReadWriteProperty<Any, T> {

    private val pref by lazy {
        LibUtil.getApp().applicationContext
            .getSharedPreferences(spName, Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return getParam(paramName, default)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        putParam(paramName, value)
    }

    private fun putParam(paramName: String, value: T) = with(pref.edit()) {
        when (value) {
            is Long -> {
                putLong(paramName, value)
            }
            is Boolean -> {
                putBoolean(paramName, value)
            }
            is Int -> {
                putInt(paramName, value)
            }
            is String -> {
                putString(paramName, value)
            }
            is Float -> {
                putFloat(paramName, value)
            }
            else -> {
                throw IllegalArgumentException("This type can't be saved into Preferences")
            }
        }
    }.apply()

    @Suppress("UNCHECKED_CAST")
    private fun <T> getParam(paramName: String, default: T): T = with(pref) {
        val result = when (default) {
            is Long -> {
                getLong(paramName, 0L)
            }
            is Boolean -> {
                getBoolean(paramName, false)
            }
            is Int -> {
                getInt(paramName, 0)
            }
            is String -> {
                getString(paramName, "")
            }
            is Float -> {
                getFloat(paramName, 0f)
            }
            else -> {
                throw IllegalArgumentException("This type can't be saved into Preferences")
            }
        }

        return result as T
    }
}