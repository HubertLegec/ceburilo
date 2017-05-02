package com.legec.ceburilo.web

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "country")
class VeturiloCountry {
    @field:Attribute(name = "lat")
    var latitude: Float = 0f
    @field:Attribute(name = "lng")
    var longtitude: Float = 0f
    @field:Attribute
    var zoom: Int = 0
    @field:Attribute
    var name: String = ""
    @field:Attribute
    var hotline: String = ""
    @field:Attribute
    var domain: String = ""
    @field:Attribute
    var country: String = ""
    @field:Attribute(name = "country_name")
    var countryName: String = ""
    @field:Attribute
    var terms: String = ""
    @field:Attribute
    var policy: String = ""
    @field:Attribute
    var website: String = ""
    @field:Attribute(name = "show_free_racks", required = false)
    var showFreeRacks: Int = 0
    @field:Attribute(name = "booked_bikes")
    var bookedBikes: Int = 0
    @field:Attribute(name = "set_point_bikes")
    var setPointBikes: Int = 0
    @field:Attribute(name = "available_bikes")
    var availableBikes: Int = 0
    @field:ElementList(inline = true, required = false)
    var cities: List<VeturiloCity> = ArrayList()
}