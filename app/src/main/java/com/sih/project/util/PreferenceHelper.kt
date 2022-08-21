package com.sih.project.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import java.lang.ref.WeakReference

object PreferenceHelper {

    private const val USER_ID = "userId"
    private const val LOGGED_IN = "userLoggedIn"
    private const val APP_SHARED_PREFS = "smartBinsPrefs"

    @SuppressLint("StaticFieldLeak")
    private lateinit var context: WeakReference<Context>

    private val prefs: SharedPreferences?
        get() = try {
            context.get()?.getSharedPreferences(APP_SHARED_PREFS, MODE_PRIVATE)
        } catch (e: Exception) {
            null
        }

    fun setContext(context: Context) {
        this.context = WeakReference(context)
    }

    @JvmStatic
    var userId: String
        get() = prefs?.getString(USER_ID, "") ?: ""
        set(value) {
            prefs?.save(USER_ID, value)
        }

    @JvmStatic
    var isLoggedIn: Boolean
        get() = prefs?.getBoolean(LOGGED_IN, false) ?: false
        set(value) {
            prefs?.save(LOGGED_IN, value)
        }


}