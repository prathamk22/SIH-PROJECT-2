package com.sih.project.util

import android.app.Activity
import android.content.Intent
import com.sih.project.ui.LoginActivity

object Utils {

    fun logout(activity: Activity){
        PreferenceHelper.userId = ""
        PreferenceHelper.isLoggedIn = false
        activity.startActivity(Intent(activity, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        activity.finish()
    }

}