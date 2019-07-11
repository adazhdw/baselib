package com.adazhdw.baselibrary.utils

import android.content.Context
import android.content.SharedPreferences
import com.adazhdw.baselibrary.LibUtil

class SPUtils {

    companion object {
        private const val SP_NAME = "base_sp_name"

        private val spMap: Map<String, SharedPreferences> by lazy {
            HashMap<String, SharedPreferences>().apply { put(SP_NAME, mSharedPreferences) }
        }

        private val mSharedPreferences: SharedPreferences by lazy {
            LibUtil.getApp().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        }
        private var mCurrentSP: SharedPreferences = mSharedPreferences

        /**
         * new SPUtils Instance
         */
        fun newInstance(name: String? = null): SPUtils {
            if (spMap.contains(name ?: SP_NAME)) {
                spMap[name ?: SP_NAME] ?: error("SharedPreferences init fail")
            } else {
                LibUtil.getApp().getSharedPreferences(name ?: SP_NAME, Context.MODE_PRIVATE)
            }.also { mCurrentSP = it }
            return SPUtils()
        }

    }


    fun <T> putParam(key: String, value: T) {
        when (value) {
            is Int -> {
                mCurrentSP.edit().putInt(key, value).apply()
            }
            is Boolean -> {
                mCurrentSP.edit().putBoolean(key, value).apply()
            }
            is String -> {
                mCurrentSP.edit().putString(key, value).apply()
            }
            is Float -> {
                mCurrentSP.edit().putFloat(key, value).apply()
            }
            is Long -> {
                mCurrentSP.edit().putLong(key, value).apply()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getParam(key: String, delValue: T): T {
        when (delValue) {
            is Int -> {
                return mCurrentSP.getInt(key, delValue) as T
            }
            is Boolean -> {
                return mCurrentSP.getBoolean(key, delValue) as T
            }
            is String -> {
                return mCurrentSP.getString(key, delValue) as T
            }
            is Float -> {
                return mCurrentSP.getFloat(key, delValue) as T
            }
            is Long -> {
                return mCurrentSP.getLong(key, delValue) as T
            }
            else -> {
                throw SPParamsIllegalException()
            }
        }
    }

    fun putMutableSet(key: String, value: MutableSet<String>) {
        mCurrentSP.edit().putStringSet(key, value).apply()
    }

    fun getMutableSet(key: String, defValue: MutableSet<String>): MutableSet<String> {
        return mCurrentSP.getStringSet(key, defValue) ?: mutableSetOf()
    }

}

class SPParamsIllegalException : Exception("sp's getParam' param is illegal")

val sp: SPUtils by lazy { SPUtils() }
val permissionSP by lazy { SPUtils.newInstance(PermissionUtils.PERMISSION_SP) }