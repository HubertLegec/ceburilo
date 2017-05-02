package com.legec.ceburilo.web


interface VeturiloPlacesCallback {
    fun onSuccess(places: List<VeturiloPlace>)
    fun onError(message: String)
}