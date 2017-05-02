package com.legec.ceburilo.web

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VeturiloApiService(private val veturiloApiClient: VeturiloApiClient) {

    fun getVeturiloPlaces(callback: VeturiloPlacesCallback) {
        val request = veturiloApiClient.getVeturiloData()
        request.enqueue(object: Callback<VeturiloMarkers> {
            override fun onResponse(call: Call<VeturiloMarkers>?, response: Response<VeturiloMarkers>?) {
                val places = response!!.body().countries
                        .first { c -> c.country == "PL" }
                        .cities
                        .first { city -> city.name == "Warszawa" }
                        .places
                callback.onSuccess(places)
            }

            override fun onFailure(call: Call<VeturiloMarkers>?, t: Throwable?) {
                callback.onError(t?.message!!)
            }

        })
    }
}