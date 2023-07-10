package com.endava.parking

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ParkingApplication: Application() {

    companion object {
        lateinit var instance: ParkingApplication private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
