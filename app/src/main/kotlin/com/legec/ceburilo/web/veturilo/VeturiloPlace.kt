package com.legec.ceburilo.web.veturilo

import com.google.android.gms.maps.model.LatLng
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

/**
 * Representation of single veturilo point from XML returned by Veturilo API
 */
@Root(name = "place")
class VeturiloPlace {
    @field:Attribute
    var uid: Int = 0
    @field:Attribute(name = "lat")
    var latitude: Double = 0.0
    @field:Attribute(name = "lng")
    var longtitude: Double = 0.0
    @field:Attribute
    var name: String = ""
    @field:Attribute
    var spot: Int = 0
    @field:Attribute(required = false)
    var number: Int = 0
    @field:Attribute
    var bikes: Int = 0
    @field:Attribute(name = "bike_racks")
    var bikeRacks: Int = 0
    @field:Attribute(name = "free_racks")
    var freeRacks: Int = 0
    @field:Attribute(name = "terminal_type", required = false)
    var terminalType: String = ""
    @field:Attribute(name = "bike_numbers", required = false)
    var bikeNumbers: String = ""
    @field:Attribute(required = false)
    var maintenance: Int = 0
    @field:Attribute(name = "bike_types", required = false)
    var bikeTypes: String = ""

    override fun toString(): String {
        return name
    }

    fun getLatLng(): LatLng {
        return LatLng(latitude, longtitude)
    }

}