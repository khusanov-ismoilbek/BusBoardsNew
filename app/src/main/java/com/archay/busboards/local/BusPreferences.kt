package com.archay.busboards.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class BusPreferences @Inject constructor(private val context: Context) {
    companion object {
        const val DEVICE_PREF = "BusPref"
    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences(DEVICE_PREF, Context.MODE_PRIVATE)

    var deviceToken: String
        get() = preferences.getString(::deviceToken.name, "") ?: ""
        set(value) = preferences.edit().putString(::deviceToken.name, value).apply()

//    var accessToken: String
//        get() = preferences.getString(::accessToken.name, "") ?: ""
//        set(value) = preferences.edit().putString(::accessToken.name, value).apply()

    var deviceId: String
        get() = preferences.getString(::deviceId.name, "") ?: ""
        set(value) = preferences.edit().putString(::deviceId.name, value).apply()

    var lastVideoPosition: Int
        get() = preferences.getInt(::lastVideoPosition.name, 0)
        set(value) = preferences.edit().putInt(::lastVideoPosition.name, value).apply()

    var slides: String
        get() = preferences.getString(::slides.name, "") ?: ""
        set(value) = preferences.edit().putString(::slides.name, value).apply()
}