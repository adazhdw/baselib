@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.adazhdw.ktlib.ext

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * author: daguozhu
 * created on: 2019/10/22 19:33
 * description:
 */

/**
 * return the SharePreference instance
 */
fun Context.sp(spName: String = packageName, mode: Int = Context.MODE_PRIVATE): SharedPreferences =
        getSharedPreferences(spName, mode)

inline fun SharedPreferences.edit(
        commit: Boolean = false,
        action: SharedPreferences.Editor.() -> Unit
) {
    val editor = edit()
    action(editor)
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}

/**
 * set a [T] into sp
 */
fun <T> Context.spPutValue(key: String, value: T, spName: String = packageName) = sp(spName).edit {
    when (value) {
        is Long -> putLong(key, value)
        is Boolean -> putBoolean(key, value)
        is Int -> putInt(key, value)
        is String -> putString(key, value)
        is Float -> putFloat(key, value)
        else -> putString(key, Gson().toJson(value))
    }
}

/**
 * get a [T] into sp
 */
@Suppress("UNCHECKED_CAST")
fun <T> Context.spGetValue(key: String, default: T, spName: String = packageName): T = sp(spName).run {
    val result = when (default) {
        is Long -> getLong(key, default)
        is Boolean -> getBoolean(key, default)
        is Int -> getInt(key, default)
        is String -> getString(key, default)
        is Float -> getFloat(key, default)
        else -> Gson().fromJson(getString(key, ""), object : TypeToken<T>() {}.type)
    }

    return result as T

}