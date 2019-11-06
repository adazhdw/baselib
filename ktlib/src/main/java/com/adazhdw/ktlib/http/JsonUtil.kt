package com.adazhdw.ktlib.http

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object JsonUtil {

    @JvmOverloads
    fun formatJson(json: String, indentSpaces: Int = 4): String {
        try {
            var i = 0
            val len = json.length
            while (i < len) {
                val c = json[i]
                if (c == '{') {
                    return JSONObject(json).toString(indentSpaces)
                } else if (c == '[') {
                    return JSONArray(json).toString(indentSpaces)
                } else if (!Character.isWhitespace(c)) {
                    return json
                }
                i++
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return json
    }
}
/**
 * 格式化json字符串
 *
 * @param json 需要格式化的json串
 * @return 格式化后的json串
 */
