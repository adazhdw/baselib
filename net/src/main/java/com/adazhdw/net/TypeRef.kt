package com.adazhdw.net

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Represents a generic type `T`. Java doesn't yet provide a way to
 * represent generic types, so this class does. Forces clients to create a
 * subclass of this class which enables retrieval the type information even at
 * runtime.
 *
 *
 * For example, to create a type literal for `List<String>`, you can
 * create an empty anonymous inner class:
 *
 *
 *
 * `TypeToken<List<String>> list = new TypeToken<List<String>>() {};`
 *
 *
 * This syntax cannot be used to create type literals that have wildcard
 * parameters, such as `Class<?>` or `List<? extends CharSequence>`.
 *
 * @author Bob Lee
 * @author Sven Mawson
 * @author Jesse Wilson
 */
open class TypeRef<T> {
    /**
     * Returns the raw (non-generic) type for this type.
     */
    val rawType: Class<in T>

    /**
     * Gets underlying `Type` instance.
     */
    val type: Type
    val hashCode: Int

    /**
     * Constructs a new type literal. Derives represented class from type
     * parameter.
     *
     *
     * Clients create an empty anonymous subclass. Doing so embeds the type
     * parameter in the anonymous class's type hierarchy so we can reconstitute it
     * at runtime despite erasure.
     */
    protected constructor() {
        type = getSuperclassTypeParameter(javaClass)
        rawType = TypeUtils.getRawType(type) as Class<in T>
        hashCode = type.hashCode()
    }

    /**
     * Unsafe. Constructs a type literal manually.
     */
    constructor(type: Type) {
        this.type = TypeUtils.canonicalize(Preconditions.checkNotNull(type))
        rawType = TypeUtils.getRawType(this.type) as Class<in T>
        hashCode = this.type.hashCode()
    }

    override fun hashCode(): Int {
        return hashCode
    }

    override fun equals(other: Any?): Boolean {
        return other is TypeRef<*> && TypeUtils.equals(type, other.type)
    }

    override fun toString(): String {
        return TypeUtils.typeToString(type)
    }

    companion object {
        /**
         * Returns the type from super class's type parameter in [ canonical form][TypeUtils.canonicalize].
         */
        fun getSuperclassTypeParameter(subclass: Class<*>): Type {
            val superclass = subclass.genericSuperclass
            if (superclass is Class<*>) {
                throw RuntimeException("Missing type parameter.")
            }
            val parameterized = superclass as ParameterizedType
            return TypeUtils.canonicalize(parameterized.actualTypeArguments[0])
        }

        /**
         * Gets type literal for the given `Type` instance.
         */
        operator fun get(type: Type): TypeRef<*> {
            return TypeRef<Any>(type)
        }

        /**
         * Gets type literal for the given `Class` instance.
         */
        operator fun <T> get(type: Class<T>): TypeRef<T> {
            return TypeRef(type)
        }

        /**
         * Gets type literal for the parameterized type represented by applying `typeArguments` to
         * `rawType`.
         */
        fun getParameterized(rawType: Type, vararg typeArguments: Type): TypeRef<*> {
            return TypeRef<Any>(TypeUtils.newParameterizedTypeWithOwner(null, rawType, *typeArguments))
        }

        /**
         * Gets type literal for the array type whose elements are all instances of `componentType`.
         */
        fun getArray(componentType: Type): TypeRef<*> {
            return TypeRef<Any>(TypeUtils.arrayOf(componentType))
        }
    }
}