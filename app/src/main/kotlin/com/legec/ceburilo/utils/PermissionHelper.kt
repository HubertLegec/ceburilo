package com.legec.ceburilo.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat


class PermissionHelper(private val activity: Activity) {
    val LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
    val LOCATION_REQUEST_CODE = 99

    fun checkLocationPermission(): Boolean {
        return checkPermission(LOCATION_PERMISSION, LOCATION_REQUEST_CODE, activity)
    }

    fun isLocationPermissionGranted(grantResults: IntArray): Boolean {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(activity, LOCATION_PERMISSION)
                    == PackageManager.PERMISSION_GRANTED) {
                return true
            }
            return false
        }
        return false
    }

    private fun checkPermission(permission: String, requestCode: Int, activity: Activity): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity,
                    permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
                return false
            }
            return true
        }
        return true
    }
}