@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.adazhdw.ktlib.ext

import android.content.SharedPreferences
import com.adazhdw.ktlib.LibUtil

/**
 * author: daguozhu
 * created on: 2019/10/22 19:33
 * description:
 */

/**
 * return the SharePreference instance
 */

class SPUtils private constructor(spName: String) {

    companion object {
        private val SP_MAP: HashMap<String, SPUtils> = hashMapOf()
        fun sp(spName: String = "SPExt"): SPUtils {
            var sp = SP_MAP[spName]
            if (sp == null) {
                sp = SPUtils(spName)
                SP_MAP[spName] = sp
            }
            return sp
        }
    }

    private val sp: SharedPreferences

    init {
        sp = LibUtil.context.sp(spName)
    }

    fun putString(key: String, value: String) {
        sp.edit { putString(key, value) }
    }

    fun getString(key: String, default: String = ""): String {
        return sp.getString(key, default)?:""
    }

    fun putLong(key: String, value: Long) {
        sp.edit { putLong(key, value) }
    }

    fun getLong(key: String, default: Long = 0): Long {
        return sp.getLong(key, default)
    }

    fun putBoolean(key: String, value: Boolean) {
        sp.edit { putBoolean(key, value) }
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return sp.getBoolean(key, default)
    }

    fun putFloat(key: String, value: Float) {
        sp.edit { putFloat(key, value) }
    }

    fun getBoolean(key: String, default: Float = 0f): Float {
        return sp.getFloat(key, default)
    }

    fun putInt(key: String,value: Int){
        sp.edit { putInt(key, value) }
    }

    fun getInt(key: String,default: Int = 0): Int {
        return sp.getInt(key,default)
    }
}
