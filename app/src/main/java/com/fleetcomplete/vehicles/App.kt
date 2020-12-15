package com.fleetcomplete.vehicles

import android.app.Application
import okhttp3.OkHttpClient

class App : Application() {
    var httpClient: OkHttpClient? = null
        private set

    override fun onCreate() {
        super.onCreate()
        app = this
        httpClient = OkHttpClient()
    }

    companion object {
        var app: App? = null
            private set
    }
}