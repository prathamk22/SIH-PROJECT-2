package com.kanyideveloper.firebasecoroutinesdemo

import android.app.Application
import com.kanyideveloper.firebasecoroutinesdemo.util.PreferenceHelper

class SmartBinsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceHelper.setContext(this)
    }

}