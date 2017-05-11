package com.legec.ceburilo.web.directions.model

import com.google.android.gms.maps.model.LatLng


class DirectionDestinationRequest(internal var apiKey: String, internal var origin: LatLng) {

    fun to(destination: LatLng): DirectionRequest {
        return DirectionRequest(apiKey, origin, destination)
    }
}