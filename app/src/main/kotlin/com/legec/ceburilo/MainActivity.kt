package com.legec.ceburilo

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.gc.materialdesign.views.Slider
import com.legec.ceburilo.utils.PermissionHelper
import com.legec.ceburilo.utils.SharedPrefsService
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
    @Inject lateinit var sharedPrefsService: SharedPrefsService
    @BindView(R.id.fromPoint)
    lateinit var fromSpinner: Spinner
    @BindView(R.id.toPoint)
    lateinit var toSpinner: Spinner
    @BindView(R.id.progressbar_view)
    lateinit var progressView: LinearLayout
    @BindView(R.id.timeSlider)
    lateinit var timeSlider: Slider
    @BindView(R.id.selectedTime)
    lateinit var selectedTime: TextView
    lateinit private var pointsAdapter: ArrayAdapter<VeturiloPlace>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        CeburiloApp.appComponent.inject(this)
        pointsAdapter = ArrayAdapter(this, R.layout.spinner, ArrayList<VeturiloPlace>())
        fromSpinner.adapter = pointsAdapter
        toSpinner.adapter = pointsAdapter
        timeSlider.value = sharedPrefsService.getRoutePartMaxTime()
        timeSlider.onValueChangedListener = Slider.OnValueChangedListener { value ->
            selectedTime.text = value.toString() + " min"
            sharedPrefsService.saveRoutePartMaxTime(value)
        }
    }

    override fun onStart() {
        if (permissionHelper.checkLocationPermission()) {
            googleLocationService.connectApiClient()
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
        if (!permissionHelper.checkLocationPermission()) {
            showPermissionNotGrantedMessage()
            return
        }
        val startPointId = fromSpinner.selectedItemId
        val endPointId = toSpinner.selectedItemId
        if (startPointId == endPointId) {
            val message = getString(R.string.start_end_point_message)
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        } else {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("START_POINT_ID", startPointId)
            intent.putExtra("END_POINT_ID", endPointId)
            startActivity(intent)
        }
    }

    @OnClick(R.id.selectClosestButton)
    fun selectNearestVeturiloPoint() {
        if (permissionHelper.checkLocationPermission()) {
            val location = getCurrentLocation()
            val closestPoint = veturiloApiService.findNearestVeturiloPlace(location.latitude, location.longitude)
            fromSpinner.setSelection(pointsAdapter.getPosition(closestPoint))
        } else {
            showPermissionNotGrantedMessage()
        }
    }

    private fun getVeturiloPlaces() {
        progressView.visibility = View.VISIBLE
        veturiloApiService.getVeturiloPlaces(object : VeturiloPlacesCallback {
            override fun onSuccess(places: List<VeturiloPlace>) {
                pointsAdapter.clear()
                pointsAdapter.addAll(places)
                pointsAdapter.notifyDataSetChanged()
                progressView.visibility = View.GONE
            }

            override fun onError(message: String) {
                progressView.visibility = View.GONE
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
                        googleLocationService.connectApiClient()
                } else {
                    showPermissionNotGrantedMessage()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showPermissionNotGrantedMessage() {
        val permissionMessage = getString(R.string.permission_message)
        Toast.makeText(this, permissionMessage, Toast.LENGTH_LONG).show()
    }
}
