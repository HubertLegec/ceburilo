package com.legec.ceburilo.web.maps

import android.location.Location


interface LocationChangedListener {
    fun onLocationChange(location: Location)
}