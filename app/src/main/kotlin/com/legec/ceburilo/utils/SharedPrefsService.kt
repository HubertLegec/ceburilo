package com.legec.ceburilo.utils

import android.content.Context
import android.content.SharedPreferences


class SharedPrefsService(val context: Context) {
    private val MAX_TIME_KEY = "ceburilo_max_time"

    private val sharedPreferences: SharedPreferences
        get() {
            return context.getSharedPreferences(MAX_TIME_KEY, Context.MODE_PRIVATE)
        }

    fun getRoutePartMaxTime(): Int {
        return sharedPreferences.getInt(MAX_TIME_KEY, 20)
    }

    fun saveRoutePartMaxTime(time: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(MAX_TIME_KEY, time)
        editor.apply()
    }
}