package com.legec.ceburilo.web.directions.model

import com.akexorcist.googledirection.model.Coordination
import com.akexorcist.googledirection.model.Info
import com.akexorcist.googledirection.model.RoutePolyline
import com.akexorcist.googledirection.model.TransitDetail
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel


@Parcel(parcelsIndex = false)
class Step {
    var distance: Info = Info()
    var duration: Info = Info()
    @SerializedName("end_location")
    var endLocation: Coordination = Coordination()
    @SerializedName("html_instructions")
    var htmlInstruction: String = ""
    var maneuver: String = ""
    @SerializedName("start_location")
    var startLocation: Coordination = Coordination()
    @SerializedName("transit_details")
    var transitDetail: TransitDetail = TransitDetail()
    @SerializedName("steps")
    var stepList: List<Step> = emptyList()
    var polyline: RoutePolyline = RoutePolyline()
    @SerializedName("travel_mode")
    var travelMode: String = ""

    val isContainStepList: Boolean
        get() = stepList.isNotEmpty()

    fun getDurationInSeconds(): Int {
        return duration.value.toInt()
    }
}