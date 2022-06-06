package com.example.diplomna.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPref {
    private lateinit var prefs: SharedPreferences
    private const val PREFS_NAME = "sharedPref"
    private const val EMPTY_STRING = ""


    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun readString(key: String): String = prefs.getString(key, EMPTY_STRING) ?: EMPTY_STRING

    fun readBoolean(key: String): Boolean = prefs.getBoolean(key, false)

    fun write(nickname : String, isChecked: Boolean){
        val editor = prefs.edit()
        editor.putString("NICKNAME",nickname)
        editor.putBoolean("CHECKBOX",isChecked)
        editor.apply()
    }

    fun clear(){
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}