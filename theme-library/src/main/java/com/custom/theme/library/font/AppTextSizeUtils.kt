package com.custom.theme.library.font

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.WrapperListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


internal object AppTextSizeUtils {

    /**
     * 当前显示的字体
     */
    private var showTextSize = AppTextSizeEnum.Normal

    internal fun getCustomTextSize(): AppTextSizeEnum {
        return showTextSize
    }

    internal fun setCustomTextSize(textSizeEnum: AppTextSizeEnum) {
        showTextSize = textSizeEnum
    }

    /**
     * 是否开启了自定义字体大小
     */
    private fun getCustomTextSizeEnabled(attrs: AttributeSet): Boolean {
        return attrs.getAttributeBooleanValue("http://schemas.android.com/apk/res-auto", "customTextSizeEnabled", false)
    }

    private fun getTextSizeResourceId(attrs: AttributeSet): Int {
        //原 TextSize 的资源ID
        return attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "textSize", 0)
    }

    /**
     * 获取原来设置的 TextSize 资源名称
     */
    private fun getTextSizeResourceName(context: Context, textSizeResourceId: Int): String? {
        if (textSizeResourceId != 0) {
            //原 TextSize 的资源名称
            return context.resources.getResourceEntryName(textSizeResourceId)
        }
        return null
    }

    /**
     * 获取替换后 TextSize 的资源ID
     */
    private fun getReplaceTextSizeResourceId(context: Context, textSizeResourceName: String): Int {
        return if (showTextSize == AppTextSizeEnum.Normal) {
            //常规字号,不改变
            context.resources.getIdentifier(textSizeResourceName, "dimen", context.packageName)
        } else {
            context.resources.getIdentifier(textSizeResourceName + showTextSize.label, "dimen", context.packageName)
        }
    }

    /**
     * 替换字体大小，页面重新创建时或者重新 setContentView 时生效
     */
    internal fun replaceTextViewSizeInflater(context: Context, name: String, attrs: AttributeSet): View? {
        if (name != "TextView") {
            return null
        }
        val customTextSizeEnabled = getCustomTextSizeEnabled(attrs)
        if (!customTextSizeEnabled) {
            return null
        }
        val textSizeResourceId = getTextSizeResourceId(attrs)
        val textSizeResourceName = getTextSizeResourceName(context, textSizeResourceId)
        if (textSizeResourceName.isNullOrEmpty()) {
            return null
        }
        val newTextSizeResourceId = if (showTextSize == AppTextSizeEnum.Normal) {
            textSizeResourceId
        } else {
            getReplaceTextSizeResourceId(context, textSizeResourceName)
        }
        if (newTextSizeResourceId == 0) {
            return null
        }
        // CustomChangeTextView 替换 TextView
        val newTextSize = context.resources.getDimension(newTextSizeResourceId)
        val textView = CustomChangeTextView(context, attrs, true, textSizeResourceName)
        //设置新的 textSize
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize)
        return textView
    }


    /**
     * 刷新当前 Activity 的所有 TextSize
     */
    internal fun refreshActivityTextSize(activity: Activity, appTextSize: AppTextSizeEnum, animate: Boolean) {
        val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
        refreshViewTextSize(rootView, appTextSize, animate)
    }

    internal fun refreshViewTextSize(view: ViewGroup, appTextSize: AppTextSizeEnum, animate: Boolean) {
        for (i in 0 until view.childCount) {
            val childView = view.getChildAt(i)
            if (childView is ViewGroup) {
                if (view is AbsListView) {
                    cleanAbsListView(view)
                }
                if (view is RecyclerView) {
                    cleanRecyclerViewCache(view)
                }
                //递归继续查找 CustomChangeTextView
                refreshViewTextSize(childView, appTextSize, animate)
            } else {
                if (childView is CustomChangeTextView) {
                    childView.refreshTextSize(appTextSize, animate)
                }
            }
        }
    }

    private val mAbsListViewField: Field? by lazy {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            try {
                val absListViewField = AbsListView::class.java.getDeclaredField("mRecycler")
                absListViewField.isAccessible = true
                return@lazy absListViewField
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        null
    }

    private val mAbsListViewClearMethod: Method? by lazy {
        try {
            val absListViewClearMethod = Class.forName("android.widget.AbsListView\$Recycler").getDeclaredMethod("clear")
            absListViewClearMethod.isAccessible = true
            return@lazy absListViewClearMethod
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        null
    }

    private fun cleanAbsListView(view: AbsListView) {
        try {
            mAbsListViewField?.get(view)?.let {
                mAbsListViewClearMethod?.invoke(it)
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        var adapter = view.adapter
        while (adapter is WrapperListAdapter) {
            adapter = adapter.wrappedAdapter
        }
        if (adapter is BaseAdapter) {
            adapter.notifyDataSetChanged()
        }
    }

    private val mRecycleViewField: Field? by lazy {
        try {
            val recycleViewField = RecyclerView::class.java.getDeclaredField("mRecycler")
            recycleViewField.isAccessible = true
            return@lazy recycleViewField
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        null
    }

    private val mRecycleViewClearMethod: Method? by lazy {
        try {
            val recycleViewClearMethod = Class.forName("androidx.recyclerview.widget.RecyclerView\$Recycler").getDeclaredMethod("clear")
            recycleViewClearMethod.isAccessible = true
            return@lazy recycleViewClearMethod
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        null
    }

    /**
     * 清理 RecyclerView 的缓存
     */
    private fun cleanRecyclerViewCache(view: RecyclerView) {
        try {
            mRecycleViewField?.get(view)?.let {
                mRecycleViewClearMethod?.invoke(it)
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        view.recycledViewPool.clear()
        view.invalidateItemDecorations()
    }

}