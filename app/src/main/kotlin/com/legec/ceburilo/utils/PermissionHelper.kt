package com.legec.ceburilo.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat


class PermissionHelper(private val context: Context) {
    private val LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
    val LOCATION_REQUEST_CODE = 1000

    fun checkLocationPermission(activity: Activity) {
        checkPermission(LOCATION_PERMISSION, LOCATION_REQUEST_CODE, activity)
    }

    private fun checkPermission(permission: String, requestCode: Int, activity: Activity) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,
                permission)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)

                // requestCode is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
}