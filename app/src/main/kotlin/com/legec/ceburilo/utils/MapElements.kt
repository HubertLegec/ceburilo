package com.legec.ceburilo.utils

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.legec.ceburilo.web.veturilo.VeturiloPlace


class MapElements(private val map: GoogleMap) {
    private val markers: MutableList<Marker> = ArrayList()
    private val polylines: MutableList<Polyline> = ArrayList()

    fun addPolylines(polylineOptions: List<PolylineOptions>) {
        polylineOptions.forEach { polOpt ->
            val polyline = map.addPolyline(polOpt)
            polylines.add(polyline)
        }
    }

    fun addWaypoints(waypoints: List<VeturiloPlace>) {
        waypoints.forEach { w ->
            val markerOptions = MarkerOptions()
                    .position(w.getLatLng())
                    .title(w.name)
                    .icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            val marker = map.addMarker(markerOptions)
            markers.add(marker)
        }
    }

    fun addMarker(title: String, latLng: LatLng) {
        val markerOptions = MarkerOptions()
                .position(latLng)
                .title(title)
        val marker = map.addMarker(markerOptions)
        markers.add(marker)
    }

    fun clearMap() {
        markers.forEach { m -> m.remove() }
        polylines.forEach { p -> p.remove() }
    }
}