package com.custom.theme.library

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.core.view.LayoutInflaterCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.custom.theme.library.font.AppTextSizeEnum
import com.custom.theme.library.font.AppTextSizeLiveData
import com.custom.theme.library.font.AppTextSizeUtils
import com.custom.theme.library.font.CustomChangeTextView

object AppCustomThemeUtils {

    /**
     * 初始设置
     */
    fun initCustomTextSize(value: AppTextSizeEnum) {
        return AppTextSizeUtils.setCustomTextSize(value)
    }

    /**
     * 外部获取文字大小
     */
    fun getCustomTextSize(): AppTextSizeEnum {
        return AppTextSizeUtils.getCustomTextSize()
    }


    /**
     * 在  setContentView 之前调用
     */
    fun setLayoutInflaterFactory(layoutInflater: LayoutInflater) {
        LayoutInflaterCompat.setFactory2(layoutInflater, object : LayoutInflater.Factory2 {
            override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
                return AppTextSizeUtils.replaceTextViewSizeInflater(context, name, attrs)
            }

            override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
                return null
            }
        })
    }

    /**
     * 文字大小改变的监听，设置自定义监听，需要传入 Observer
     */
    fun bindingTextSizeChange(lifecycleOwner: LifecycleOwner, observer: Observer<AppTextSizeEnum>? = null) {
        AppTextSizeLiveData.bindingTextSizeChangeListener(lifecycleOwner, observer)
    }

    /**
     * 解绑自定义监听
     */
    fun unBindingTextSizeChange(observer: Observer<AppTextSizeEnum>) {
        AppTextSizeLiveData.unBindingTextSizeChangeListener(observer)
    }

    /**
     * 刷新当前页面的字体大小，仅显示。不会保存数据
     */
    fun refreshViewTextSize(lifecycleOwner: LifecycleOwner, value: AppTextSizeEnum) {
        if (lifecycleOwner is AppCompatActivity) {
            val rootView = lifecycleOwner.findViewById<ViewGroup>(android.R.id.content)
            AppTextSizeUtils.refreshViewTextSize(rootView, value, true)
        } else if (lifecycleOwner is AppCompatDialog) {
            val rootView = lifecycleOwner.findViewById<ViewGroup>(android.R.id.content)
            if (rootView != null) {
                AppTextSizeUtils.refreshViewTextSize(rootView, value, true)
            }
        }
    }

    /**
     * 刷新单个View,ViewGroup
     */
    fun refreshViewTextSize(view: View, value: AppTextSizeEnum, animate: Boolean) {
        if (view is ViewGroup) {
            AppTextSizeUtils.refreshViewTextSize(view, value, animate)
        } else {
            if (view is CustomChangeTextView) {
                view.refreshTextSize(value, animate)
            }
        }
    }

    /**
     * 保存设置，并且通知所有页面，更新文字大小
     */
    fun saveAppTextSize(newValue: AppTextSizeEnum) {
        if (AppTextSizeUtils.getCustomTextSize() == newValue) {
            return
        }
        //保存更新后的值
        AppTextSizeUtils.setCustomTextSize(newValue)
        //通知页面刷新
        AppTextSizeLiveData.updateTextSize(newValue)
    }

}