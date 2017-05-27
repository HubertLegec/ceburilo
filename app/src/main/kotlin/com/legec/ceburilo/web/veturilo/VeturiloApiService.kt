package com.legec.ceburilo.web.veturilo

import com.legec.ceburilo.utils.pythagorasEquirectangular
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VeturiloApiService(private val veturiloApiClient: VeturiloApiClient) {
    private var lastFetchedPlaces: List<VeturiloPlace> = ArrayList()

    fun getVeturiloPlaces(callback: VeturiloPlacesCallback) {
        val request = veturiloApiClient.getVeturiloData()
        request.enqueue(object: Callback<VeturiloMarkers> {
            override fun onResponse(call: Call<VeturiloMarkers>?, response: Response<VeturiloMarkers>?) {
                val places = response!!.body().countries
                        .first { c -> c.country == "PL" }
                        .cities
                        .first { city -> city.name == "Warszawa" }
                        .places
                        // only places where we can return our bike
                        .filter { place -> place.freeRacks > 0 }
                        // only places that have bikes available
                        .filter { place -> place.bikes > 0 }
                lastFetchedPlaces = places
                callback.onSuccess(places)
            }

            override fun onFailure(call: Call<VeturiloMarkers>?, t: Throwable?) {
                callback.onError(t?.message!!)
            }

        })
    }

    fun getLastFetchedPlaces(): List<VeturiloPlace> {
        return lastFetchedPlaces
    }

    fun getPlaceById(id: Long): VeturiloPlace {
        return lastFetchedPlaces[id.toInt()]
    }

    fun findNearestVeturiloPlace(latitude: Double, longitude: Double): VeturiloPlace? {
        val veturiloPlaces = getLastFetchedPlaces()
        return veturiloPlaces.minBy { place ->
            pythagorasEquirectangular(latitude, longitude, place.latitude, place.longtitude)
        }
    }
}