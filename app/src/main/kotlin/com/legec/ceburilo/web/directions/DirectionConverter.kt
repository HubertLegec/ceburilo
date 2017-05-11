package com.legec.ceburilo.web.directions

import android.content.Context
import android.util.DisplayMetrics
import com.akexorcist.googledirection.model.Step
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import java.util.*


object DirectionConverter {
    fun getDirectionPoint(stepList: List<Step>?): ArrayList<LatLng> {
        val directionPointList = ArrayList<LatLng>()
        stepList?.forEach { step ->
            convertStepToPosition(step, directionPointList)
        }
        return directionPointList
    }

    private fun convertStepToPosition(step: Step, directionPointList: ArrayList<LatLng>) {
        // Get start location
        directionPointList.add(step.startLocation.coordination)
        // Get encoded points location
        if (step.polyline != null) {
            val decodedPointList = step.polyline.pointList
            decodedPointList?.forEach { position ->
                directionPointList.add(position)
            }
        }
        // Get end location
        directionPointList.add(step.endLocation.coordination)
    }

    fun getSectionPoint(stepList: List<Step>?): ArrayList<LatLng> {
        val directionPointList = ArrayList<LatLng>()
        if (stepList != null && stepList.isNotEmpty()) {
            // Get start location only first position
            directionPointList.add(stepList[0].startLocation.coordination)
            stepList.forEach { step ->
                directionPointList.add(step.endLocation.coordination)
            }
        }
        return directionPointList
    }

    fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val position = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
            poly.add(position)
        }
        return poly
    }

    fun createPolyline(context: Context, locationList: ArrayList<LatLng>, width: Int, color: Int): PolylineOptions {
        val rectLine = PolylineOptions().width(dpToPx(context, width).toFloat()).color(color).geodesic(true)
        locationList.forEach { location ->
            rectLine.add(location)
        }
        return rectLine
    }

    fun createTransitPolyline(context: Context, stepList: List<Step>?, transitWidth: Int, transitColor: Int, walkingWidth: Int, walkingColor: Int): ArrayList<PolylineOptions> {
        val polylineOptionsList = ArrayList<PolylineOptions>()
        stepList?.forEach { step ->
            val directionPointList = ArrayList<LatLng>()
            convertStepToPosition(step, directionPointList)
            if (step.isContainStepList) {
                polylineOptionsList.add(createPolyline(context, directionPointList, walkingWidth, walkingColor))
            } else {
                polylineOptionsList.add(createPolyline(context, directionPointList, transitWidth, transitColor))
            }
        }
        return polylineOptionsList
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}