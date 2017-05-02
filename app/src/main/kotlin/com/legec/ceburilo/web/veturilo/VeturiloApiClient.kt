package com.legec.ceburilo.web.veturilo

import retrofit2.http.GET


interface VeturiloApiClient {
    @GET("/maps/nextbike-official.xml?city=210")
    fun getVeturiloData(): retrofit2.Call<VeturiloMarkers>
}