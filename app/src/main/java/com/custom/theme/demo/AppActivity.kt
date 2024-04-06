package com.custom.theme.demo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.custom.theme.library.AppCustomThemeUtils

inline fun <reified VB : ViewBinding> ComponentActivity.binding(crossinline inflate: (LayoutInflater, ViewGroup, Boolean) -> VB) = lazy(
    LazyThreadSafetyMode.NONE
) {
    inflate(layoutInflater, window.decorView as ViewGroup, false).also { binding ->
        setContentView(binding.root)
        if (binding is ViewDataBinding) binding.lifecycleOwner = this
    }
}

fun <VB : ViewBinding> Dialog.binding(inflate: (LayoutInflater) -> VB) = lazy {
    inflate(layoutInflater).also { setContentView(it.root) }
}

abstract class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCustomThemeUtils.setLayoutInflaterFactory(layoutInflater)
        super.onCreate(savedInstanceState)
        AppCustomThemeUtils.bindingTextSizeChange(this)
    }

}