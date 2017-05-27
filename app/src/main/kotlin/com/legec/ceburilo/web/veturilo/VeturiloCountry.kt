package com.legec.ceburilo.web.veturilo

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * Representation of country structure from Veturilo API XML
 */
@Root(name = "country")
class VeturiloCountry {
    @field:Attribute(name = "lat")
    var latitude: Double = 0.0
    @field:Attribute(name = "lng")
    var longtitude: Double = 0.0
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