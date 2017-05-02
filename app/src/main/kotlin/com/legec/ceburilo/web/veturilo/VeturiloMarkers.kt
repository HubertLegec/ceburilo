package com.legec.ceburilo.web.veturilo

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


@Root(name = "markers")
class VeturiloMarkers {
    @field:ElementList(inline = true)
    var countries: List<VeturiloCountry> = ArrayList()
}