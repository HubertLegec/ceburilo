package com.legec.ceburilo.web.directions.model


object GoogleDirection {
    fun withServerKey(apiKey: String): DirectionOriginRequest {
        return DirectionOriginRequest(apiKey)
    }
}