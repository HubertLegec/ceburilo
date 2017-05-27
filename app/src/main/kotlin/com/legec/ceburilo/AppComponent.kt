package com.legec.ceburilo

import com.legec.ceburilo.web.WebModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, WebModule::class))
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mapsActivity: MapsActivity)
}