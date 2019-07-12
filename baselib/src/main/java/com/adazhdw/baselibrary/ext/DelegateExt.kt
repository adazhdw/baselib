package com.adazhdw.baselibrary.ext

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * kotlin单例 属性委托
 */
object DelegateExt {
    fun <T> notNullSingleValue(): ReadWriteProperty<Any?, T> = NotNullSingleValueVar()

    fun <T> preference(name: String, default: T, spName: String = "base_sp_name"): Preference<T> =
        Preference(spName, name, default)
}

class NotNullSingleValueVar<T : Any?> : ReadWriteProperty<Any?, T> {
    private var value: T? = null
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return this.value ?: throw IllegalArgumentException("${property.name} not initialized")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value =
            if (this.value == null) value else throw IllegalArgumentException("${property.name} already initialized")
    }

}