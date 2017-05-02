package com.legec.ceburilo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import com.legec.ceburilo.web.maps.GoogleLocationService
import com.legec.ceburilo.web.veturilo.VeturiloApiService
import com.legec.ceburilo.web.veturilo.VeturiloPlace
import com.legec.ceburilo.web.veturilo.VeturiloPlacesCallback
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private val TAG = "MAIN_ACTIVITY"
    @Inject lateinit var veturiloApiService: VeturiloApiService
    @Inject lateinit var googleLocationService: GoogleLocationService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        CeburiloApp.webComponent.inject(this)
    }

    override fun onStart() {
        googleLocationService.connectApiClient(this)
        super.onStart()
    }

    override fun onStop() {
        googleLocationService.disconnectApiClient()
        super.onStop()
    }

    @OnClick(R.id.button)
    fun onDownloadButtonClick(view: View) {
        Log.i(TAG, "button click")
        veturiloApiService.getVeturiloPlaces(object: VeturiloPlacesCallback {
            override fun onSuccess(places: List<VeturiloPlace>) {
                Log.i(TAG, "places count: " + places.size)
            }

            override fun onError(message: String) {
                Log.e(TAG, message)
            }

        })

        val l = googleLocationService.getCurrentLocation()
        Log.i(TAG, "Location:" + l.latitude + ", " + l.longitude)
    }
}
