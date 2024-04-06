package com.custom.theme.demo

import android.app.Application
import com.custom.theme.library.AppCustomThemeUtils
import com.custom.theme.library.font.AppTextSizeEnum

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCustomThemeUtils.initCustomTextSize(AppTextSizeEnum.Normal)
    }

}