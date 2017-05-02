package com.legec.ceburilo.web.maps

import android.content.Context
import android.location.Location
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices


class GoogleLocationService {
    private var googleApiClient: GoogleApiClient? = null

    fun connectApiClient(context: Context) {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(context)
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
}