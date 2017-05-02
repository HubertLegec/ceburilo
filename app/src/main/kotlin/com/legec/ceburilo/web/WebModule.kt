package com.legec.ceburilo.web

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton


@Module
class WebModule {
    private val API_BASE_URL = "https://nextbike.net/"

    @Provides
    @Singleton
    fun providesVeturiloClient(): VeturiloApiClient {
        val retrofit = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()
        return retrofit.create(VeturiloApiClient::class.java)
    }

    @Provides
    @Singleton
    fun providesVeturiloService(veturiloApiClient: VeturiloApiClient): VeturiloApiService {
        return VeturiloApiService(veturiloApiClient)
    }
}