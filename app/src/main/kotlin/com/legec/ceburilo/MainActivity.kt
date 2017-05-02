package com.legec.ceburilo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import com.legec.ceburilo.web.VeturiloApiService
import com.legec.ceburilo.web.VeturiloPlace
import com.legec.ceburilo.web.VeturiloPlacesCallback
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private val TAG = "MAIN_ACTIVITY"
    @Inject lateinit var veturiloApiService: VeturiloApiService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        CeburiloApp.webComponent.inject(this)
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
    }
}
