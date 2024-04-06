package com.custom.theme.library.font

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView

/**
 * 自定义 修改字体大小的 TextView
 *
 * @param textSizeEnabled 是否开启字体大小的替换
 * @param textSizeResourceName 保存的文字常规字体大小的资源名称
 */
internal class CustomChangeTextView(
    private val context: Context,
    private val attrs: AttributeSet?,
    private val textSizeEnabled: Boolean = false,
    private val textSizeResourceName: String? = null
) : AppCompatTextView(context, attrs) {

    /**
     * 刷新字体大小：当页面已经显示时，直接刷新页面字体大小
     *
     * @param animate 搞个动画
     */
    internal fun refreshTextSize(appTextSize: AppTextSizeEnum, animate: Boolean) {
        if (!textSizeEnabled) {
            return
        }
        val newTextSizeResourceId = if (appTextSize == AppTextSizeEnum.Normal) {
            context.resources.getIdentifier(textSizeResourceName, "dimen", context.packageName)
        } else {
            context.resources.getIdentifier(textSizeResourceName + appTextSize.label, "dimen", context.packageName)
        }
        if (newTextSizeResourceId != 0) {
            val newTextSize = context.resources.getDimension(newTextSizeResourceId)
            if (animate) {
                val animator = ValueAnimator.ofFloat(textSize, newTextSize)
                animator.addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Float
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, animatedValue)
                }
                animator.duration = 80
                animator.start()
            } else {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize)
            }
        }
    }

}