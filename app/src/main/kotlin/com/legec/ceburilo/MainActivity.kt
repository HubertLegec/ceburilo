package com.legec.ceburilo

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.legec.ceburilo.utils.PermissionHelper
import com.legec.ceburilo.web.maps.GoogleLocationService
import com.legec.ceburilo.web.veturilo.VeturiloApiService
import com.legec.ceburilo.web.veturilo.VeturiloPlace
import com.legec.ceburilo.web.veturilo.VeturiloPlacesCallback
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private val TAG = "MAIN_ACTIVITY"
    private val permissionHelper = PermissionHelper(this)
    @Inject lateinit var veturiloApiService: VeturiloApiService
    @Inject lateinit var googleLocationService: GoogleLocationService
    @BindView(R.id.fromPoint)
    lateinit var fromSpinner: Spinner
    @BindView(R.id.toPoint)
    lateinit var toSpinner: Spinner
    lateinit private var pointsAdapter: ArrayAdapter<VeturiloPlace>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        CeburiloApp.webComponent.inject(this)
        pointsAdapter = ArrayAdapter(this, R.layout.spinner, ArrayList<VeturiloPlace>())
        fromSpinner.adapter = pointsAdapter
        toSpinner.adapter = pointsAdapter
    }

    override fun onStart() {
        if (permissionHelper.checkLocationPermission()) {
            googleLocationService.connectApiClient(this)
        }
        getVeturiloPlaces()
        super.onStart()
    }

    override fun onStop() {
        googleLocationService.disconnectApiClient()
        super.onStop()
    }

    @OnClick(R.id.findButton)
    fun onFindButtonClick(view: View) {
        Log.i(TAG, "button click")
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    private fun getVeturiloPlaces() {
        veturiloApiService.getVeturiloPlaces(object : VeturiloPlacesCallback {
            override fun onSuccess(places: List<VeturiloPlace>) {
                pointsAdapter.clear()
                pointsAdapter.addAll(places)
                pointsAdapter.notifyDataSetChanged()
            }

            override fun onError(message: String) {
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                Log.e(TAG, message)
            }

        })
    }

    private fun getCurrentLocation(): Location {
        val l = googleLocationService.getCurrentLocation()
        Log.d(TAG, "Location:" + l.latitude + ", " + l.longitude)
        return l
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            permissionHelper.LOCATION_REQUEST_CODE -> {
                if (permissionHelper.isLocationPermissionGranted(grantResults)) {
                        googleLocationService.connectApiClient(this)
                } else {
                    // permission denied
                    // TODO Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
