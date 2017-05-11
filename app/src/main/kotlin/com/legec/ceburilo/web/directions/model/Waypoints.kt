package com.legec.ceburilo.web.directions.model

import com.google.android.gms.maps.model.LatLng


class Waypoints {
    private var points: List<LatLng> = ArrayList()

    fun addPoints(points: List<LatLng>) {
        this.points = points
    }

    fun toParamString(): String? {
        if (points.isEmpty()) {
            return null
        }
        return points.map { p ->
            p.latitude.toString() + "," + p.longitude.toString()
        }.joinToString("|")
    }

}