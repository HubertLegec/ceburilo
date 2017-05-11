package com.legec.ceburilo.web.directions.model

import com.google.android.gms.maps.model.LatLng


class DirectionRequestParam(
        public val apiKey: String,
        val origin: LatLng,
        val destination: LatLng) {
    var transportMode: String? = null
    var departureTime: String? = null
    var language: String? = null
    var unit: String? = null
    var avoid: String? = null
    var transitMode: String? = null
    var isAlternatives: Boolean = false

}