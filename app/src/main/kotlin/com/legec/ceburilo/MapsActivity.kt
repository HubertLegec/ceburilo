package com.legec.ceburilo

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import butterknife.BindString
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.legec.ceburilo.utils.MapElements
import com.legec.ceburilo.utils.SharedPrefsService
import com.legec.ceburilo.web.directions.GoogleDirectionsService
import com.legec.ceburilo.web.directions.RouteCallback
import com.legec.ceburilo.web.maps.GoogleLocationService
import com.legec.ceburilo.web.veturilo.VeturiloApiService
import com.legec.ceburilo.web.veturilo.VeturiloPlace
import javax.inject.Inject


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val TAG = "MAPS_ACTIVITY"
    @BindString(R.string.start_point_id)
    lateinit var startPointIdDescriptor: String
    @BindString(R.string.end_point_id)
    lateinit var endPointIdDescriptor: String
    @BindString(R.string.dystans)
    lateinit var distanceText: String
    @BindString(R.string.czas_przejazdu)
    lateinit var timeText: String
    @BindString(R.string.time_left)
    lateinit var timeLeftText: String
    @BindView(R.id.distanceTV)
    lateinit var distanceTV: TextView
    @BindView(R.id.durationTV)
    lateinit var durationTV: TextView
    @BindView(R.id.progressbar_view)
    lateinit var progressbarView: LinearLayout
    @BindView(R.id.startStopButton)
    lateinit var startStopButton: Button
    @BindView(R.id.timeLeftTV)
    lateinit var timeLeftTV: TextView
    @Inject lateinit var googleLocationService: GoogleLocationService
    @Inject lateinit var veturiloApiService: VeturiloApiService
    @Inject lateinit var googleDirectionsService: GoogleDirectionsService
    @Inject lateinit var sharedPrefsService: SharedPrefsService
    private var mMap: GoogleMap? = null
    private var startPoint: VeturiloPlace? = null
    private var endPoint: VeturiloPlace? = null
    private var mapElements: MapElements? = null
    private var timer: CountDownTimer? = null

    /**
     * Get route callback.
     * On success list of route parts, intermediate points, distance and duration is returned.
     * On error message is presented to user.
     */
    private val callback = object: RouteCallback {
        override fun onSuccess(polylines: List<PolylineOptions>, distance: String, duration: String, waypoints: List<VeturiloPlace>) {
            updateMap(polylines, waypoints)
            distanceTV.text = distanceText + " " + distance
            durationTV.text = timeText + " " + duration
            timeLeftTV.text = timeLeftText + " " + sharedPrefsService.getRoutePartMaxTime() + ":00"
            progressbarView.visibility = View.GONE
            Log.i(TAG, "Success")
        }

        override fun onError(message: String?) {
            Toast.makeText(this@MapsActivity, message, Toast.LENGTH_LONG).show()
            Log.e(TAG, message)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        ButterKnife.bind(this)
        CeburiloApp.appComponent.inject(this)
        updateStartAndEndPoint()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @OnClick(R.id.startStopButton)
    fun onStartTimeClick() {
        if (startStopButton.text == "Reset") {
            timer?.cancel()
            startStopButton.setTextColor(resources.getColor(R.color.colorPrimaryDark))
        }

        val interval = 1000
        val milisInFuture = sharedPrefsService.getRoutePartMaxTime() * 60 * 1000
        timer = object: CountDownTimer(milisInFuture.toLong(), interval.toLong()) {
            override fun onFinish() {
                timeLeftTV.text = "Koniec czasu!"
                timeLeftTV.setTextColor(Color.RED)
                startStopButton.text = "Start"
            }

            override fun onTick(millisUntilFinished: Long) {
                val secondsTotal = millisUntilFinished / 1000
                val minutes = secondsTotal / 60
                val seconds = secondsTotal - minutes * 60
                timeLeftTV.text = timeLeftText + " " + minutes.toString() + ":" + seconds.toString()
            }

        }
        timer?.start()
        startStopButton.text = "Reset"
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapElements = MapElements(googleMap)
        mMap?.isMyLocationEnabled = true
        val currLoc = googleLocationService.getCurrentLocation()
        val latLong = LatLng(currLoc.latitude, currLoc.longitude)
        val zoomLevel = 16.toFloat()
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, zoomLevel))
        getRoute()
    }

    /**
     * Get route between given start and end point.
     * Clear the map before request.
     */
    private fun getRoute() {
        progressbarView.visibility = View.VISIBLE
        mapElements?.clearMap()
        googleDirectionsService.getRoute(
                startPoint!!.getLatLng(),
                endPoint!!.getLatLng(),
                callback
        )
    }

    /**
     * Update start and end point of the route.
     * Get data from intent extra.
     */
    private fun updateStartAndEndPoint() {
        val startPointId = intent.getLongExtra(startPointIdDescriptor, 0)
        val endPointId = intent.getLongExtra(endPointIdDescriptor, 0)
        startPoint = veturiloApiService.getPlaceById(startPointId)
        endPoint = veturiloApiService.getPlaceById(endPointId)
    }

    /**
     * Draw route on the map.
     * Draw start, end and intermediate points on the map.
     * @param polylines route parts
     * @param waypoints intermediate points
     */
    private fun updateMap(polylines: List<PolylineOptions>, waypoints: List<VeturiloPlace>) {
        mapElements?.addPolylines(polylines)
        mapElements?.addWaypoints(waypoints)
        mapElements?.addMarker(startPoint!!.name, startPoint!!.getLatLng())
        mapElements?.addMarker(endPoint!!.name, endPoint!!.getLatLng())
    }
}
