package com.legec.ceburilo.web.directions

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DirectionAndPlaceConnection {
    val MAPS_API_URL = "https://maps.googleapis.com/maps/api/"

    private var service: DirectionAndPlaceService? = null

    fun createService(): DirectionAndPlaceService {
        if (service == null) {
            val retrofit = Retrofit.Builder()
                    .baseUrl(MAPS_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            service = retrofit.create(DirectionAndPlaceService::class.java)
        }
        return service!!
    }

    companion object {
        private var connection: DirectionAndPlaceConnection? = null

        val instance: DirectionAndPlaceConnection
            get() {
                if (connection == null) {
                    connection = DirectionAndPlaceConnection()
                }
                return connection!!
            }
    }
}