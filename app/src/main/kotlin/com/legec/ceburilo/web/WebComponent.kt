package com.legec.ceburilo.web

import com.legec.ceburilo.MainActivity
import com.legec.ceburilo.MapsActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(WebModule::class))
interface WebComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mapsActivity: MapsActivity)
}