package com.custom.theme.demo

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.custom.theme.demo.databinding.ActivityMainBinding


class MainActivity : AppActivity() {

    private val mBinding by binding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toolbar: Toolbar = mBinding.toolbar

        mBinding.imgSetting.setColorFilter(Color.WHITE);
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        mBinding.imgSetting.setOnClickListener {
            showSettingDialog()
        }
    }


    private fun showSettingDialog() {
        val dialog = SettingDialog(this)
        dialog.show()
    }

}