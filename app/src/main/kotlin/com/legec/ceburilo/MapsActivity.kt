package com.legec.ceburilo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.legec.ceburilo.web.maps.GoogleLocationService
import javax.inject.Inject



class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    @Inject lateinit var googleLocationService: GoogleLocationService
    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        CeburiloApp.webComponent.inject(this)
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
    }
}
