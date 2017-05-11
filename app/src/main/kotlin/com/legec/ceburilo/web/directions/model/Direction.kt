package com.legec.ceburilo.web.directions.model

import com.akexorcist.googledirection.constant.RequestResult
import com.akexorcist.googledirection.model.GeocodedWaypoint
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel


@Parcel(parcelsIndex = false)
class Direction {
    @SerializedName("geocoded_waypoints")
    var geocodedWaypointList: List<GeocodedWaypoint>? = null
    @SerializedName("routes")
    var routeList: List<Route>? = null
    var status: String? = null
    @SerializedName("error_message")
    var errorMessage: String? = null
    val isOK: Boolean
        get() = status == RequestResult.OK
}