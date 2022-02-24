package com.adazhdw.ktlib.ext

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
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

class FragmentViewBindingDelegate2<T : ViewBinding>(
    val fragment: Fragment,
    val viewBindingFactory: (View) -> T
) : ReadOnlyProperty<Fragment, T> {
    private var binding: T? = null

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            val viewLifecycleOwnerLiveDataObserver = Observer<LifecycleOwner?> {
                val viewLifecycleOwner = it ?: return@Observer

                viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                    override fun onDestroy(owner: LifecycleOwner) {
                        binding = null
                    }
                })
            }

            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observeForever(viewLifecycleOwnerLiveDataObserver)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.removeObserver(viewLifecycleOwnerLiveDataObserver)
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding = binding
        if (binding != null) {
            return binding
        }

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Should not attempt to get bindings when Fragment views are destroyed.")
        }

        return viewBindingFactory(thisRef.requireView()).also { this.binding = it }
    }
}

fun <T : ViewBinding> Fragment.viewBind2(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate2(this, viewBindingFactory)

