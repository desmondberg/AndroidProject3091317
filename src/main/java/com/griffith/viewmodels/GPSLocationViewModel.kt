package com.griffith.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint


//view model for storing the current location of the user and the locations where they started and ended their journey
class GPSLocationViewModel : ViewModel() {
    var currentLocation = mutableStateOf<GeoPoint?>(null)
    var startLocation = mutableStateOf<GeoPoint?>(null)
    var endLocation = mutableStateOf<GeoPoint?>(null)
}