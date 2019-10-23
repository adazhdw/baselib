package com.adazhdw.ktlib.ext

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.IllegalArgumentException

/**
 * author: daguozhu
 * created on: 2019/10/22 19:33
 * description:
 */

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
 * return the SharePreference instance
 */
fun FragmentActivity.sp(spName: String = packageName, mode: Int = Context.MODE_PRIVATE): SharedPreferences =
    getSharedPreferences(spName, mode)

/**
 * set a [T] into sp
 */
fun <T> FragmentActivity.spPutValue(key: String, value: T, spName: String = packageName) = sp(spName).edit {
    when (value) {
        is Long -> putLong(key, value)
        is Boolean -> putBoolean(key, value)
        is Int -> putInt(key, value)
        is String -> putString(key, value)
        is Float -> putFloat(key, value)
        else -> putString(key,Gson().toJson(value))
    }
}
/**
 * get a [T] into sp
 */
@Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
fun <T> FragmentActivity.spGetValue(key: String, default: T, spName: String = packageName):T = sp(spName).run {
    val result = when (default) {
        is Long -> getLong(key, default)
        is Boolean -> getBoolean(key, default)
        is Int -> getInt(key, default)
        is String -> getString(key, default)
        is Float -> getFloat(key, default)
        else -> Gson().fromJson(getString(key,""),object :TypeToken<T>(){}.type)
    }

    return result as T

}