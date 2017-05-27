package com.legec.ceburilo.web.maps

import android.content.Context
import android.location.Location
import android.os.Bundle
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices


class GoogleLocationService(private val context: Context): GoogleApiClient.ConnectionCallbacks {
    private var googleApiClient: GoogleApiClient? = null
    private val locationChangeListeners: MutableList<LocationChangedListener> = ArrayList()
    private val mLocationRequest = LocationRequest()

    fun connectApiClient() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build()
        }
        googleApiClient?.connect()
    }

    fun disconnectApiClient() {
        googleApiClient?.disconnect()
    }

    fun getCurrentLocation(): Location {
        return LocationServices.FusedLocationApi
                .getLastLocation(googleApiClient)
    }

    fun registerLocationChangeListener(listener: LocationChangedListener) {
        if (!locationChangeListeners.contains(listener)) {
            locationChangeListeners.add(listener)
        }
    }

    fun onLocationChanged(location: Location?) {
        if (location!= null) {
            locationChangeListeners.forEach { l -> l.onLocationChange(location) }
        }
    }

    override fun onConnected(p0: Bundle?) {
        val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
        if (mLastLocation != null) {
            onLocationChanged(mLastLocation)
        }
        mLocationRequest.interval = 5000 //5 seconds
        mLocationRequest.fastestInterval = 3000 //3 seconds
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        LocationServices.FusedLocationApi
                .requestLocationUpdates(googleApiClient, mLocationRequest, {l -> onLocationChanged(l)})
    }

    override fun onConnectionSuspended(p0: Int) {}
}