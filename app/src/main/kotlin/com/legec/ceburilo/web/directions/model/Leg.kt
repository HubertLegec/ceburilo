package com.legec.ceburilo.web.directions.model

import com.akexorcist.googledirection.model.Coordination
import com.akexorcist.googledirection.model.Info
import com.akexorcist.googledirection.model.TimeInfo
import com.akexorcist.googledirection.model.Waypoint
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.legec.ceburilo.web.directions.DirectionConverter
import org.parceler.Parcel
import java.util.*


@Parcel(parcelsIndex = false)
class Leg {
    @SerializedName("arrival_time")
    var arrivalTime: TimeInfo = TimeInfo()
    @SerializedName("departure_time")
    var departureTime: TimeInfo = TimeInfo()
    var distance: Info = Info()
    var duration: Info = Info()
    @SerializedName("duration_in_traffic")
    var durationInTraffic: Info = Info()
    @SerializedName("end_address")
    var endAddress: String = ""
    @SerializedName("end_location")
    var endLocation: Coordination = Coordination()
    @SerializedName("start_address")
    var startAddress: String = ""
    @SerializedName("start_location")
    var startLocation: Coordination = Coordination()
    @SerializedName("steps")
    var stepList: List<Step> = emptyList()
    @SerializedName("via_waypoint")
    var viaWaypointList: List<Waypoint> = emptyList()

    val directionPoint: ArrayList<LatLng>
        get() = DirectionConverter.getDirectionPoint(stepList)

    val sectionPoint: ArrayList<LatLng>
        get() = DirectionConverter.getSectionPoint(stepList)

    fun getDurationInSeconds(): Int {
        return duration.value.toInt()
    }

    fun getDistanceInMeters(): Int {
        return distance.value.toInt()
    }
}