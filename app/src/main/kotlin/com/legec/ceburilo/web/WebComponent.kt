package com.legec.ceburilo.web

import com.legec.ceburilo.MainActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(WebModule::class))
interface WebComponent {
    fun inject(mainActivity: MainActivity)
}