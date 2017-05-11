package com.legec.ceburilo.web.directions.model


interface DirectionCallback {
    fun onDirectionSuccess(direction: Direction, rawBody: String)
    fun onDirectionFailure(t: Throwable)
}