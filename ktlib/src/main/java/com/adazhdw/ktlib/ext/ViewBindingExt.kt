package com.adazhdw.ktlib.ext

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.adazhdw.ktlib.core.lifecycle.addOnDestroy
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * ViewBinding Extension function
 */


inline fun <reified T : ViewBinding> Activity.viewBinding(layoutInflater: LayoutInflater): T {
    return T::class.java.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as T
}

inline fun <reified T : ViewBinding> Activity.viewBind() = lazy {
    viewBinding<T>(layoutInflater).apply { setContentView(this.root) }
}


/**
 * Fragment ViewBinding
 * The inflate() method requires you to pass in a layout inflater.
 * If the layout has already been inflated,
 * you can instead call the binding class's static bind() method.
 * To learn more, see an example from the view binding GitHub sample.
 */

class FragmentViewBindingDelegate<T : ViewBinding>(private val clazz: Class<T>) : ReadOnlyProperty<Fragment, T> {
    private var binding: T? = null

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (binding == null) {
            binding = clazz.getMethod("bind", View::class.java).invoke(null, thisRef.requireView()) as T
            thisRef.viewLifecycleOwner.addOnDestroy {
                binding = null
            }
        }
        return binding!!
    }
}

inline fun <reified T : ViewBinding> Fragment.viewBind() = FragmentViewBindingDelegate<T>(T::class.java)

