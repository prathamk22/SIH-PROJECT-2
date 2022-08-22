package com.sih.project

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.sih.project.util.PreferenceHelper

class SmartBinsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceHelper.setContext(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //For night mode theme
    }

}