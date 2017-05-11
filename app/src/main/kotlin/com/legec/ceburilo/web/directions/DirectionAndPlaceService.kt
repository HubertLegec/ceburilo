package com.legec.ceburilo.web.directions

import com.legec.ceburilo.web.directions.model.Direction
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface DirectionAndPlaceService {

    @GET("directions/json")
    fun getDirection(@Query("origin") origin: String,
                     @Query("destination") destination: String,
                     @Query("mode") transportMode: String?,
                     @Query("departure_time") departureTime: String?,
                     @Query("language") language: String?,
                     @Query("units") units: String?,
                     @Query("avoid") avoid: String?,
                     @Query("transit_mode") transitMode: String?,
                     @Query("alternatives") alternatives: Boolean?,
                     @Query("waypoints") waypoints: String?,
                     @Query("key") apiKey: String): Call<Direction>
}