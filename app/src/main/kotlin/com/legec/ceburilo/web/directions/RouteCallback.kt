package com.legec.ceburilo.web.directions

import com.google.android.gms.maps.model.PolylineOptions
import com.legec.ceburilo.web.veturilo.VeturiloPlace


interface RouteCallback {
    fun onSuccess(polylines: List<PolylineOptions>, distance: String, duration: String, waypoints: List<VeturiloPlace>)
    fun onError(message: String?)
}