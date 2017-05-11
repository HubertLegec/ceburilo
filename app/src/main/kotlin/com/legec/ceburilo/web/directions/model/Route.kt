package com.legec.ceburilo.web.directions.model

import com.akexorcist.googledirection.model.Bound
import com.akexorcist.googledirection.model.Fare
import com.akexorcist.googledirection.model.RoutePolyline
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel


@Parcel(parcelsIndex = false)
class Route {
    // waypoint_order

    @SerializedName("bounds")
    var bound: Bound? = null
    var copyrights: String? = null
    @SerializedName("legs")
    var legList: List<Leg> = emptyList()
    @SerializedName("overview_polyline")
    var overviewPolyline: RoutePolyline? = null
    var summary: String? = null
    var fare: Fare? = null
    @SerializedName("warnings")
    var warningList: List<String>? = null

    fun getDurationInSeconds(): Int {
        return legList
                .map { l -> l.getDurationInSeconds() }
                .sum()
    }

    fun getDistanceInMeters(): Int {
        return legList
                .map { l -> l.getDistanceInMeters() }
                .sum()
    }
}
