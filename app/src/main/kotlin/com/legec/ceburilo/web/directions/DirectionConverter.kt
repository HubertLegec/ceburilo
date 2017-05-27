package com.legec.ceburilo.web.directions

import android.content.Context
import android.util.DisplayMetrics
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.legec.ceburilo.web.directions.model.Step
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
        val decodedPointList = step.polyline.pointList
        decodedPointList?.forEach { position ->
            directionPointList.add(position)
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

    fun createPolyline(context: Context, locationList: ArrayList<LatLng>, width: Int, color: Int): PolylineOptions {
        val rectLine = PolylineOptions().width(dpToPx(context, width).toFloat()).color(color).geodesic(true)
        locationList.forEach { location ->
            rectLine.add(location)
        }
        return rectLine
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}