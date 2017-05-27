package com.legec.ceburilo.web.veturilo

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


/**
 * Representation of one of the levels of Veturilo API XML
 */
@Root(name = "markers")
class VeturiloMarkers {
    @field:ElementList(inline = true)
    var countries: List<VeturiloCountry> = ArrayList()
}