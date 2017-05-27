package com.legec.ceburilo.web.directions

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.akexorcist.googledirection.model.Coordination
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.legec.ceburilo.utils.SharedPrefsService
import com.legec.ceburilo.web.directions.model.*
import com.legec.ceburilo.web.veturilo.VeturiloApiService
import com.legec.ceburilo.web.veturilo.VeturiloPlace
import java.text.DecimalFormat


class GoogleDirectionsService(
        private val serverKey: String,
        private val context: Context,
        private val veturiloApiService: VeturiloApiService,
        private val sharedPrefsService: SharedPrefsService) {
    private val TAG = "DIRECTION_SERVICE"
    private var waypoints: List<VeturiloPlace> = emptyList()

    /**
     * Returns route between given points.
     * It splits route into sections according to given max single section time.
     * @param startPoint coordinates of start point
     * @param endPoint coordinates of end point
     * @param callback result callback
     */
    fun getRoute(startPoint: LatLng, endPoint: LatLng, callback: RouteCallback) {
        waypoints = emptyList()
        val directionCallback = object : DirectionCallback {
            override fun onDirectionSuccess(direction: Direction, rawBody: String) {
                Log.i(TAG, "Success")
                if (direction.isOK) {
                    val numberOfWayPartsNeeded = numberOfWayPartsNeeded(direction)
                    val numberOfLegs = direction.getRoute().legList.size
                    if (numberOfWayPartsNeeded > 0 && numberOfLegs == 1) {
                        waypoints = determineWayPoints(direction, numberOfWayPartsNeeded)
                        val waypointsLatLng = waypoints.map { w -> w.getLatLng() }
                        executeSearchQuery(startPoint, endPoint, waypointsLatLng, this)
                    } else {
                        val polylines = getPolylinesFromDirection(direction)
                        val distance = getDistanceFromDirection(direction)
                        val duration = getTimeFromDirection(direction)
                        callback.onSuccess(polylines, distance, duration, waypoints)
                    }
                } else {
                    callback.onError("Nie udało się znaleźć trasy: " + direction.errorMessage)
                }
            }

            override fun onDirectionFailure(t: Throwable) {
                Log.e(TAG, t.message)
                callback.onError(t.message)
            }

        }
        executeSearchQuery(startPoint, endPoint, emptyList(), directionCallback)
    }

    /**
     * Convert route legs to polylines, that can be drawn on the map
     * @param direction route
     * @return list of polylines
     */
    private fun getPolylinesFromDirection(direction: Direction): List<PolylineOptions> {
        return direction.getRoute().legList
                .map { leg ->
                    DirectionConverter.createPolyline(context, leg.directionPoint, 4, Color.BLUE)
                }
    }

    /**
     * Returns distance in kilometers for given route
     * @param direction route
     * @return distance in kilometers
     */
    private fun getDistanceFromDirection(direction: Direction): String {
        val formatter = DecimalFormat("#.##")
        return formatter.format(direction.getRoute().getDistanceInMeters() / 1000.0) + " km"
    }

    /**
     * Returns time in minutes for given route
     * @param direction route
     * @return time in minutes
     */
    private fun getTimeFromDirection(direction: Direction): String {
        val durationInSeconds = direction.getRoute().getDurationInSeconds()
        return (durationInSeconds / 60).toString() + " min"
    }

    /**
     * Find route between given start and end point.
     * Optionally it is possible to give list of intermediate points.
     * @param startPoint start point of the route
     * @param endPoint end point of the route
     * @param via list of intermediate points. May be empty.
     * @param directionCallback callback with search response
     */
    private fun executeSearchQuery(startPoint: LatLng, endPoint: LatLng,
                                   via: List<LatLng>, directionCallback: DirectionCallback) {
        GoogleDirection.withServerKey(serverKey)
                .from(startPoint)
                .to(endPoint)
                .transportMode(TransportMode.BICYCLING)
                .language(Language.POLISH)
                .waypoints(via)
                .execute(directionCallback)
    }

    /**
     * Returns number of parts our way must be split into.
     * Each part shouldn't last longer than the given time
     * @param direction route returned by google API
     * @return number of parts needed
     */
    private fun numberOfWayPartsNeeded(direction: Direction): Int {
        val seconds = direction.routeList[0].getDurationInSeconds()
        val maxSeconds = (sharedPrefsService.getRoutePartMaxTime() - 2) * 60
        return seconds / maxSeconds
    }

    /**
     *  Returns intermediate points for given route and number of expected points
     *  @param direction route that should be split into segments
     *  @param numberOfWayParts expected number of sections
     *  @return list of intermediate points (Veturilo points)
     */
    private fun determineWayPoints(direction: Direction, numberOfWayParts: Int): List<VeturiloPlace> {
        val route = direction.getRoute()
        if (route.legList.size > 1) {
            return emptyList()
        }
        val leg = route.legList[0]
        val totalTime = leg.getDurationInSeconds()
        return IntRange(1, numberOfWayParts)
                .map { v -> v.toDouble() / (numberOfWayParts + 1) * totalTime }
                .map { t -> findWaypointLocationForTime(t, leg) }
                .map { l -> veturiloApiService.findNearestVeturiloPlace(l.latitude, l.longitude) }
                .distinct()
                .requireNoNulls()
    }

    /**
     * Returns location of the most distant point we can reach in given time.
     * Point is a connection of any two sections of our way.
     * @param t maximal time
     * @param leg way from start to end point
     * @return location of point on the way
     */
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