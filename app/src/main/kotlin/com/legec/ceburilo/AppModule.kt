package com.legec.ceburilo

import android.content.Context
import com.legec.ceburilo.utils.SharedPrefsService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun providesSharedPrefsService(): SharedPrefsService {
        return SharedPrefsService(context)
    }
}