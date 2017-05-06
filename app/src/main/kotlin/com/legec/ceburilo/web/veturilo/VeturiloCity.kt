package com.legec.ceburilo.web.veturilo

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "city")
class VeturiloCity {
    @field:Attribute
    var uid: Int = 0
    @field:Attribute(name = "lat")
    var latitude: Double = 0.0
    @field:Attribute(name = "lng")
    var longtitude: Double = 0.0
    @field:Attribute
    var zoom: Int = 0
    @field:Attribute(name = "maps_icon")
    var mapsIcon: String = ""
    @field:Attribute
    var alias: String = ""
    @field:Attribute(name = "break")
    var breakVal: Int = 0
    @field:Attribute
    var name: String = ""
    @field:Attribute(name = "num_places")
    var numPlaces: Int = 0
    @field:Attribute(name = "refresh_rate")
    var refreshRate: Int = 0
    @field:Attribute
    var bounds: String = ""
    @field:Attribute(name = "booked_bikes")
    var bookedBikes: Int = 0
    @field:Attribute(name = "set_point_bikes")
    var setPointBikes: Int = 0
    @field:Attribute(name = "available_bikes")
    var availableBikes: Int = 0
    @field:ElementList(inline = true)
    var places: List<VeturiloPlace> = ArrayList()
}