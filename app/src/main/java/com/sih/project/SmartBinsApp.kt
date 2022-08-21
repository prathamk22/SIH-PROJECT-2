package com.sih.project

import android.app.Application
import com.sih.project.util.PreferenceHelper

class SmartBinsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceHelper.setContext(this)
    }

}