package com.example.diplomna

import android.app.Application
import com.example.diplomna.util.SharedPref

class AutoIns : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPref.init(this)
    }
}