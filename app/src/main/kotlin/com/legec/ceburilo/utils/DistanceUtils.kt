package com.legec.ceburilo.utils


private fun deg2Rad(deg: Double): Double {
    return deg * Math.PI / 180
}

fun pythagorasEquirectangular(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val lat1r = deg2Rad(lat1)
    val lat2r = deg2Rad(lat2)
    val lon1r = deg2Rad(lon1)
    val lon2r = deg2Rad(lon2)
    val R = 6371 // km
    val x = (lon2r - lon1r) * Math.cos((lat1r + lat2r) / 2)
    val y = (lat2r - lat1r)
    return Math.sqrt(x * x + y * y) * R
}