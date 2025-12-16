package com.griffith.viewmodels

import android.location.Location
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint

//view model for storing the current location of the user and the locations where they started and ended their journey
class GPSLocationViewModel : ViewModel() {
    var currentLocation = mutableStateOf<GeoPoint?>(null)
    var startLocation = mutableStateOf<GeoPoint?>(null)
    var endLocation = mutableStateOf<GeoPoint?>(null)

    //path-drawing
    val pathPoints = mutableStateListOf<GeoPoint>()

    fun addPathPoint(point: GeoPoint) {
        pathPoints.add(point)
    }

    //distance calculation
    fun calculateTotalDistanceMeters(): Float {
        var totalDistance = 0f
        //from i = 1, until i = the size of pathPoints
        for (i in 1 until pathPoints.size) {
            val prev = pathPoints[i - 1]
            val curr = pathPoints[i]

            val results = FloatArray(1)
            //calculate distance between two points using the distanceBetween function
            Location.distanceBetween(
                prev.latitude,
                prev.longitude,
                curr.latitude,
                curr.longitude,
                results
            )

            totalDistance += results[0]
        }

        return totalDistance
    }

}