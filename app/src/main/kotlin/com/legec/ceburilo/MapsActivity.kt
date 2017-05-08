package com.legec.ceburilo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import butterknife.BindString
import butterknife.ButterKnife
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.legec.ceburilo.web.directions.GoogleDirectionsService
import com.legec.ceburilo.web.directions.RouteCallback
import com.legec.ceburilo.web.maps.GoogleLocationService
import com.legec.ceburilo.web.veturilo.VeturiloApiService
import javax.inject.Inject



class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val TAG = "MAPS_ACTIVITY"
    @BindString(R.string.start_point_id)
    lateinit var startPointIdDescriptor: String
    @BindString(R.string.end_point_id)
    lateinit var endPointIdDescriptor: String
    @Inject lateinit var googleLocationService: GoogleLocationService
    @Inject lateinit var veturiloApiService: VeturiloApiService
    @Inject lateinit var googleDirectionsService: GoogleDirectionsService
    private var mMap: GoogleMap? = null
    private var startPointId: Long? = null
    private var endPointId: Long? = null
    private var routePolyline: Polyline? = null

    private val callback = object: RouteCallback {
        override fun onSuccess(polyline: PolylineOptions, distance: String, duration: String) {
            this@MapsActivity.routePolyline =mMap?.addPolyline(polyline)
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
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        ButterKnife.bind(this)
        CeburiloApp.webComponent.inject(this)
        startPointId = intent.getLongExtra(startPointIdDescriptor, 0)
        endPointId = intent.getLongExtra(endPointIdDescriptor, 0)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
        mMap?.isMyLocationEnabled = true
        val currLoc = googleLocationService.getCurrentLocation()
        val latLong = LatLng(currLoc.latitude, currLoc.longitude)
        val zoomLevel = 16.toFloat()
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, zoomLevel))
        showRoute()
    }

    private fun showRoute() {
        routePolyline?.remove()
        val startPoint = veturiloApiService.getPlaceById(startPointId!!)
        val endPoint = veturiloApiService.getPlaceById(endPointId!!)
        val startLatLng = LatLng(startPoint.latitude, startPoint.longtitude)
        val endLatLng = LatLng(endPoint.latitude, endPoint.longtitude)
        googleDirectionsService.getRoute(startLatLng, endLatLng, callback)
    }
}
