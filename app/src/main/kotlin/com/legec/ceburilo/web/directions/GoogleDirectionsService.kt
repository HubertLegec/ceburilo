package com.legec.ceburilo.web.directions

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.legec.ceburilo.web.directions.model.*


class GoogleDirectionsService(private val serverKey: String, private val context: Context) {
    private val TAG = "DIRECTION_SERVICE"

    fun getRoute(startPoint: LatLng, endPoint: LatLng, callback: RouteCallback) {
        val directionCallback = object: DirectionCallback {
            override fun onDirectionSuccess(direction: Direction, rawBody: String) {
                Log.i(TAG, "Success")
                if (direction.isOK) {
                    val polyline = getPolylineFromDirection(direction)
                    val distance = getDistanceFromDirection(direction)
                    val duration = getTimeFromDirection(direction)
                    callback.onSuccess(polyline, distance, duration)
                } else {
                    callback.onError("Nie udało się znaleźć trasy")
                }
            }

            override fun onDirectionFailure(t: Throwable) {
                Log.e(TAG, t.message)
                callback.onError(t.message)
            }

        }
        executeSearchQuery(startPoint, endPoint, directionCallback)
    }

    private fun getPolylineFromDirection(direction: Direction): PolylineOptions {
        return DirectionConverter.createPolyline(
                context,
                direction.routeList!![0].legList!![0].directionPoint,
                4,
                Color.BLUE
        )
    }

    private fun getDistanceFromDirection(direction: Direction): String {
        return direction.routeList!![0].legList!![0].distance.text
    }

    private fun getTimeFromDirection(direction: Direction): String {
        return direction.routeList!![0].legList!![0].duration.text
    }

    private fun executeSearchQuery(startPoint: LatLng, endPoint: LatLng, directionCallback: DirectionCallback) {
        GoogleDirection.withServerKey(serverKey)
                .from(startPoint)
                .to(endPoint)
                .transportMode(TransportMode.BICYCLING)
                .language(Language.POLISH)
                .execute(directionCallback)
    }
}