package com.kanyideveloper.firebasecoroutinesdemo.util

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast

fun SharedPreferences.save(key: String, value: Any) {
    val edit = this.edit()
    with(edit) {
        when (value) {
            is Int -> putInt(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            is Boolean -> putBoolean(key, value)
            else -> putString(key, value.toString())
        }
    }
    edit.apply()
}

fun Context.showToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT)
        .show()
}
