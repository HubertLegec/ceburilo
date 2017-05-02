package com.legec.ceburilo.web

import retrofit2.Call
import retrofit2.http.GET


interface VeturiloApiClient {
    @GET("/maps/nextbike-official.xml?city=210")
    fun getVeturiloData(): Call<VeturiloMarkers>
}