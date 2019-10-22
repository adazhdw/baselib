package com.adazhdw.ktlib.utils

import android.content.Context
import android.content.SharedPreferences
import com.adazhdw.ktlib.LibUtil
import java.lang.IllegalArgumentException

class SPUtil {

    companion object {
        private const val SP_NAME = "base_sp_name"

        /**
         * new SPUtil Instance
         */
        val INSTANCE: SPUtil by lazy { SPUtil() }

    }

    private val spMap: Map<String, SharedPreferences>
    private val mSharedPreferences: SharedPreferences
    private var mCurrentSP: SharedPreferences

    init {
        mSharedPreferences = LibUtil.getApp().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        spMap = HashMap()
        spMap.put(SP_NAME, mSharedPreferences)
        mCurrentSP = mSharedPreferences
        initSp()
    }

    fun initSp(name: String? = null) {
        if (spMap.contains(name ?: SP_NAME)) {
            spMap[name ?: SP_NAME] ?: error("SharedPreferences init fail")
        } else {
            LibUtil.getApp().getSharedPreferences(name ?: SP_NAME, Context.MODE_PRIVATE)
        }.also { mCurrentSP = it }
    }


    fun <T> putParam(key: String, value: T) = with(mCurrentSP.edit()) {
        when (value) {
            is Int -> {
                putInt(key, value)
            }
            is Boolean -> {
                putBoolean(key, value)
            }
            is String -> {
                putString(key, value)
            }
            is Float -> {
                putFloat(key, value)
            }
            is Long -> {
                putLong(key, value)
            }
            else -> {
                throw IllegalArgumentException("This type can't be saved into Preferences")
            }
        }
    }.apply()

    @Suppress("UNCHECKED_CAST")
    fun <T> getParam(key: String, delValue: T): T = with(mCurrentSP) {
        return when (delValue) {
            is Int -> {
                getInt(key, delValue) as T
            }
            is Boolean -> {
                getBoolean(key, delValue) as T
            }
            is String -> {
                getString(key, delValue) as T
            }
            is Float -> {
                getFloat(key, delValue) as T
            }
            is Long -> {
                getLong(key, delValue) as T
            }
            else -> {
                throw IllegalArgumentException("This type can't be saved into Preferences")
            }
        }
    }

    fun putMutableSet(key: String, value: Set<String>) {
        mCurrentSP.edit().putStringSet(key, value).apply()
    }

    fun getMutableSet(key: String, defValue: Set<String>): Set<String> {
        return mCurrentSP.getStringSet(key, defValue) ?: setOf()
    }

}

val spUtils by lazy { SPUtil.INSTANCE }

val PERMISSION_SP: SPUtil by lazy { SPUtil.INSTANCE.apply { initSp(PermissionUtil.PERMISSION_SP_NAME) } }