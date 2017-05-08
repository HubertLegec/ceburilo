package com.legec.ceburilo

import android.app.Application
import com.legec.ceburilo.web.DaggerWebComponent
import com.legec.ceburilo.web.WebComponent
import com.legec.ceburilo.web.WebModule


class CeburiloApp: Application() {

    companion object {
        lateinit var webComponent: WebComponent
    }

    override fun onCreate() {
        super.onCreate()
        webComponent = DaggerWebComponent.builder()
                .webModule(WebModule(this))
                .build()
    }
}