package com.legec.ceburilo.web.directions

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.akexorcist.googledirection.model.Coordination
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.legec.ceburilo.web.directions.model.*
import com.legec.ceburilo.web.veturilo.VeturiloApiService
import com.legec.ceburilo.web.veturilo.VeturiloPlace
import java.text.DecimalFormat


class GoogleDirectionsService(private val serverKey: String, private val context: Context, private val veturiloApiService: VeturiloApiService) {
    private val TAG = "DIRECTION_SERVICE"
    private var waypoints: List<VeturiloPlace> = emptyList()

    fun getRoute(startPoint: LatLng, endPoint: LatLng, callback: RouteCallback) {
        waypoints = emptyList()
        val directionCallback = object : DirectionCallback {
            override fun onDirectionSuccess(direction: Direction, rawBody: String) {
                Log.i(TAG, "Success")
                if (direction.isOK) {
                    val numberOfWaypointsNeeded = numberOfWaypointsNeeded(direction)
                    val numberOfLegs = direction.routeList[0].legList.size
                    if (numberOfWaypointsNeeded > 0 && numberOfLegs == 1) {
                        waypoints = determineWaypoints(direction, numberOfWaypointsNeeded)
                        val waypointsLatLng = waypoints.map { w -> w.getLatLng() }
                        executeSearchQuery(startPoint, endPoint, waypointsLatLng, this)
                    } else {
                        val polylines = getPolylinesFromDirection(direction)
                        val distance = getDistanceFromDirection(direction)
                        val duration = getTimeFromDirection(direction)
                        callback.onSuccess(polylines, distance, duration, waypoints)
                    }
                } else {
                    callback.onError("Nie udało się znaleźć trasy")
                }
            }

            override fun onDirectionFailure(t: Throwable) {
                Log.e(TAG, t.message)
                callback.onError(t.message)
            }

        }
        executeSearchQuery(startPoint, endPoint, emptyList(), directionCallback)
    }

    private fun getPolylinesFromDirection(direction: Direction): List<PolylineOptions> {
        return direction.routeList[0].legList.map { leg ->
            DirectionConverter.createPolyline(
                    context,
                    leg.directionPoint,
                    4,
                    Color.BLUE
            )
        }
    }

    private fun getDistanceFromDirection(direction: Direction): String {
        val formatter = DecimalFormat("#.##")
        return formatter.format(direction.routeList[0].getDistanceInMeters() / 1000.0) + " km"
    }

    private fun getTimeFromDirection(direction: Direction): String {
        return (direction.routeList[0].getDurationInSeconds() / 60).toString() + " min"
    }

    private fun executeSearchQuery(startPoint: LatLng, endPoint: LatLng, via: List<LatLng>, directionCallback: DirectionCallback) {
        GoogleDirection.withServerKey(serverKey)
                .from(startPoint)
                .to(endPoint)
                .transportMode(TransportMode.BICYCLING)
                .language(Language.POLISH)
                .waypoints(via)
                .execute(directionCallback)
    }

    private fun numberOfWaypointsNeeded(direction: Direction): Int {
        val seconds = direction.routeList[0].getDurationInSeconds()
        //18 min
        val maxSeconds = 1080
        return seconds / maxSeconds
    }

    private fun determineWaypoints(direction: Direction, numberOfWaypoints: Int): List<VeturiloPlace> {
        if (direction.routeList[0].legList.size > 1) {
            return emptyList()
        }
        val leg = direction.routeList[0].legList[0]
        val totalTime = leg.getDurationInSeconds()
        val places = IntRange(1, numberOfWaypoints)
                .map { v -> v.toDouble() / (numberOfWaypoints + 1) * totalTime }
                .map { t -> findWaypointLocationForTime(t, leg) }
                .map { l -> veturiloApiService.findNearestVeturiloPlace(l.latitude, l.longitude) }
        return places
                .distinct()
                .requireNoNulls()
    }

    private fun findWaypointLocationForTime(t: Double, leg: Leg): Coordination {
        var idx = 0
        var time = leg.stepList[idx].getDurationInSeconds()
        while (time < t && idx < leg.stepList.size) {
            idx += 1
            time += leg.stepList[idx].getDurationInSeconds()
        }
        return leg.stepList[idx].startLocation
    }
}