package com.legec.ceburilo

import android.content.Context
import com.legec.ceburilo.utils.SharedPrefsService
import com.legec.ceburilo.web.directions.GoogleDirectionsService
import com.legec.ceburilo.web.maps.GoogleLocationService
import com.legec.ceburilo.web.veturilo.VeturiloApiClient
import com.legec.ceburilo.web.veturilo.VeturiloApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton


@Module
class AppModule(private val context: Context) {
    private val API_BASE_URL = "https://nextbike.net/"

    @Provides
    @Singleton
    fun providesSharedPrefsService(): SharedPrefsService {
        return SharedPrefsService(context)
    }

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

    @Provides
    @Singleton
    fun providesGoogleLocationService(): GoogleLocationService {
        return GoogleLocationService(context)
    }

    @Provides
    @Singleton
    fun providesGoogleDirectionsService(veturiloApiService: VeturiloApiService,
                                        sharedPrefsService: SharedPrefsService): GoogleDirectionsService {
        val googleApiKey = context.getString(R.string.google_directions_key)
        return GoogleDirectionsService(googleApiKey, context, veturiloApiService, sharedPrefsService)
    }
}