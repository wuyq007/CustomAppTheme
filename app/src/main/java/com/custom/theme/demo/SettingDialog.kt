package com.custom.theme.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import com.custom.theme.demo.databinding.DialogSettingBinding
import com.custom.theme.library.AppCustomThemeUtils
import com.custom.theme.library.font.AppTextSizeEnum

class SettingDialog(private val activity: AppCompatActivity) : AppCompatDialog(activity) {

    private val mBinding by binding(DialogSettingBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCustomThemeUtils.setLayoutInflaterFactory(layoutInflater)
        AppCustomThemeUtils.bindingTextSizeChange(this)

        when (AppCustomThemeUtils.getCustomTextSize()) {
            AppTextSizeEnum.Small -> mBinding.radioSmall.isChecked = true
            AppTextSizeEnum.Normal -> mBinding.radioNormal.isChecked = true
            AppTextSizeEnum.Large -> mBinding.radioLarge.isChecked = true
            AppTextSizeEnum.Huge -> mBinding.radioHuge.isChecked = true
        }

        mBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                mBinding.radioSmall.id -> {
                    changeTextSize(AppTextSizeEnum.Small)
                }
                mBinding.radioNormal.id -> {
                    changeTextSize(AppTextSizeEnum.Normal)
                }
                mBinding.radioLarge.id -> {
                    changeTextSize(AppTextSizeEnum.Large)
                }
                mBinding.radioHuge.id -> {
                    changeTextSize(AppTextSizeEnum.Huge)
                }
            }
        }
        setOnDismissListener {
            //保存字体大小，并通知所有页面更新
            AppCustomThemeUtils.saveAppTextSize(showTextSizeValue)
        }
    }

    private var showTextSizeValue = AppCustomThemeUtils.getCustomTextSize()

    private fun changeTextSize(value: AppTextSizeEnum) {
        showTextSizeValue = value
        AppCustomThemeUtils.refreshViewTextSize(activity, value)
        AppCustomThemeUtils.refreshViewTextSize(this, value)
    }

}