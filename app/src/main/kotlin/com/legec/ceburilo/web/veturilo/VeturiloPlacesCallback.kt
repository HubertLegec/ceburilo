package com.legec.ceburilo.web.veturilo


interface VeturiloPlacesCallback {
    fun onSuccess(places: List<VeturiloPlace>)
    fun onError(message: String)
}