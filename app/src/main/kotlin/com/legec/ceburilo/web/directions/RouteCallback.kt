package com.legec.ceburilo.web.directions

import com.google.android.gms.maps.model.PolylineOptions


interface RouteCallback {
    fun onSuccess(polyLine: PolylineOptions, distance: String, duration: String)
    fun onError(message: String?)
}