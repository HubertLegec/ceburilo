package com.legec.ceburilo

import android.app.Application
import com.legec.ceburilo.web.WebModule


class CeburiloApp: Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .webModule(WebModule(this))
                .build()
    }
}