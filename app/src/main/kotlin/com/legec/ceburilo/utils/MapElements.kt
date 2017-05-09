package com.legec.ceburilo.utils

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*


class MapElements(private val map: GoogleMap) {
    private val markers: MutableList<Marker> = ArrayList()
    private val polylines: MutableList<Polyline> = ArrayList()

    fun addPolyline(polylineOptions: PolylineOptions) {
        val polyline = map.addPolyline(polylineOptions)
        polylines.add(polyline)
    }

    fun addMarker(title: String, latLng: LatLng) {
        val markerOptions = MarkerOptions()
                .position(latLng)
                .title(title);
        val marker = map.addMarker(markerOptions)
        markers.add(marker)
    }

    fun clearMap() {
        markers.forEach { m -> m.remove() }
        polylines.forEach { p -> p.remove() }
    }
}