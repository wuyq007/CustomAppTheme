package com.custom.theme.library.font

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


internal object AppTextSizeLiveData {

    /**
     * 文字字体
     */
    private val appTextSize: MutableLiveData<AppTextSizeEnum> by lazy {
        MutableLiveData()
    }

    /**
     * 文字大小改变的监听
     */
    internal fun bindingTextSizeChangeListener(lifecycleOwner: LifecycleOwner, observer: Observer<AppTextSizeEnum>? = null) {
        val appTextSizeObserver = observer ?: Observer<AppTextSizeEnum> { newTextSizeType ->
            if (lifecycleOwner is AppCompatActivity) {
                val rootView = lifecycleOwner.findViewById<ViewGroup>(android.R.id.content)
                AppTextSizeUtils.refreshViewTextSize(rootView, newTextSizeType, false)
            } else if (lifecycleOwner is AppCompatDialog) {
                val rootView = lifecycleOwner.findViewById<ViewGroup>(android.R.id.content)
                if (rootView != null) {
                    AppTextSizeUtils.refreshViewTextSize(rootView, newTextSizeType, false)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                appTextSize.removeObserver(appTextSizeObserver)
            }
        })
        appTextSize.observe(lifecycleOwner, appTextSizeObserver)
    }

    internal fun unBindingTextSizeChangeListener(observer: Observer<AppTextSizeEnum>) {
        appTextSize.removeObserver(observer)
    }

    /**
     * 更新文字大小
     */
    internal fun updateTextSize(newValue: AppTextSizeEnum) {
        if (appTextSize.value == newValue) {
            return
        }
        appTextSize.postValue(newValue)
    }

}