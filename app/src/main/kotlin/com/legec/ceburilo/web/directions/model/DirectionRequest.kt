package com.legec.ceburilo.web.directions.model

import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.legec.ceburilo.web.directions.DirectionAndPlaceConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DirectionRequest(apiKey: String, origin: LatLng, destination: LatLng) {
    private val param = DirectionRequestParam(apiKey, origin, destination)

    fun transportMode(transportMode: String): DirectionRequest {
        param.transportMode = transportMode
        return this
    }

    fun language(language: String): DirectionRequest {
        param.language = language
        return this
    }

    fun unit(unit: String): DirectionRequest {
        param.unit = unit
        return this
    }

    fun avoid(avoid: String): DirectionRequest {
        var oldAvoid: String? = param.avoid
        if (oldAvoid != null && !oldAvoid.isEmpty()) {
            oldAvoid += "|"
        } else {
            oldAvoid = ""
        }
        oldAvoid += avoid
        param.avoid = oldAvoid
        return this
    }

    fun transitMode(transitMode: String): DirectionRequest {
        var oldTransitMode: String? = param.transitMode
        if (oldTransitMode != null && !oldTransitMode.isEmpty()) {
            oldTransitMode += "|"
        } else {
            oldTransitMode = ""
        }
        oldTransitMode += transitMode
        param.transitMode = oldTransitMode
        return this
    }

    fun alternativeRoute(alternative: Boolean): DirectionRequest {
        param.isAlternatives = alternative
        return this
    }

    fun departureTime(time: String): DirectionRequest {
        param.departureTime = time
        return this
    }

    fun execute(callback: DirectionCallback?) {
        val direction = DirectionAndPlaceConnection.instance
                .createService()
                .getDirection(param.origin.latitude.toString() + "," + param.origin.longitude.toString(),
                        param.destination.latitude.toString() + "," + param.destination.longitude.toString(),
                        param.transportMode,
                        param.departureTime,
                        param.language,
                        param.unit,
                        param.avoid,
                        param.transitMode,
                        param.isAlternatives,
                        param.apiKey)

        direction.enqueue(object : Callback<Direction> {
            override fun onResponse(call: Call<Direction>, response: Response<Direction>) {
                callback?.onDirectionSuccess(response.body(), Gson().toJson(response.body()))
            }

            override fun onFailure(call: Call<Direction>, t: Throwable) {
                callback!!.onDirectionFailure(t)
            }
        })
    }
}