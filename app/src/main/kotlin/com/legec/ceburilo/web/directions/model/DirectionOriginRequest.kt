package com.legec.ceburilo.web.directions.model

import com.google.android.gms.maps.model.LatLng


class DirectionOriginRequest(private val apiKey: String) {

    fun from(origin: LatLng): DirectionDestinationRequest {
        return DirectionDestinationRequest(apiKey, origin)
    }
}