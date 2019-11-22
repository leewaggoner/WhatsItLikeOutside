package com.wreckingball.whatsitlikeoutside.utils

import android.content.SharedPreferences

class PreferencesWrapper(sharedPreferences: SharedPreferences) {
    private val preferences = sharedPreferences

    fun getInt(key: String, default: Int) : Int {
        return preferences.getInt(key, default)
    }

    fun putInt(key: String, value: Int) {
        preferences.edit().also {
            it.putInt(key, value)
            it.apply()
        }
    }
}