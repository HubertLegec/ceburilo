package com.legec.ceburilo.web.veturilo

import com.legec.ceburilo.utils.pythagorasEquirectangular
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VeturiloApiService(private val veturiloApiClient: VeturiloApiClient) {
    private var lastFetchedPlaces: List<VeturiloPlace> = ArrayList()

    /**
     * Returns list of available veturilo points and store it in lastFetchedPlaces
     * Only points which have empty slots and at least one bike are returned
     * @param callback result callback
     */
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

    /**
     * Return list of veturilo points saved by getVeturiloPlaces method
     * @return list of veturilo points
     */
    fun getLastFetchedPlaces(): List<VeturiloPlace> {
        return lastFetchedPlaces
    }

    /**
     * Find veturilo point on the list of last fetched places
     */
    fun getPlaceById(id: Long): VeturiloPlace {
        return lastFetchedPlaces[id.toInt()]
    }

    /**
     * Find veturilo place nearest to given position
     * @param latitude position latitude
     * @param longitude position longitude
     * @return nearest veturilo point
     */
    fun findNearestVeturiloPlace(latitude: Double, longitude: Double): VeturiloPlace? {
        val veturiloPlaces = getLastFetchedPlaces()
        return veturiloPlaces.minBy { place ->
            pythagorasEquirectangular(latitude, longitude, place.latitude, place.longtitude)
        }
    }
}