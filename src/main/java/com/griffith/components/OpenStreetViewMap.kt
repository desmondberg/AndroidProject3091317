package com.griffith.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.griffith.viewmodels.GPSLocationViewModel
import com.griffith.viewmodels.StopwatchViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


@SuppressLint("MissingPermission")

@Composable
fun OpenStreetViewMap(modifier: Modifier = Modifier, locationViewModel: GPSLocationViewModel, stopwatchViewModel: StopwatchViewModel) {
    val context = LocalContext.current

    //these objects should only be created the first time this component is loaded

    //user's current coordinates
    val currentLocation = locationViewModel.currentLocation
    //google play location client
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    //template for the location request
    val locationRequest = remember {
        //update every 5 seconds
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
            .setMinUpdateIntervalMillis(500L)
            .build()
    }
    //function to be called after a location request
    val locationCallback = remember {
        //make an override for the abstract LocationCallback()'s onLocationResult() function
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation
                if (location != null) {
                    val point = GeoPoint(location.latitude, location.longitude)
                    currentLocation.value = point
                    //if stopwatch is running, add the current point to the path points list
                    if(stopwatchViewModel.isRunning){
                        locationViewModel.addPathPoint(point)
                    }
                }
            }

        }
    }
    //at start-up, check if the app has access to the ACCESS_FINE_LOCATION permission, if so, continue on to retrieve user's position
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    //provide context
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
    //wrap OSMdroid map view in AndroidView
    AndroidView(
        factory = { context ->
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(16.0)
                controller.setCenter(GeoPoint(52.33, -6.28))

                //initialise path overlay
                val pathOverlay = Polyline()
                pathOverlay.outlinePaint.strokeWidth = 8f
                overlays.add(pathOverlay)
                //store the path overlay inside the map view's tag field so that it can be accessed inside the update function
                this.tag = pathOverlay
            }
        },
        //when current location updates, update the position of the marker on the map
        update = { mapView ->
            val pathOverlay = mapView.tag as Polyline

            //update location marker
            currentLocation.value?.let { location ->
                mapView.controller.setCenter(location)

                mapView.overlays.removeAll { it is Marker }
                mapView.overlays.add(
                    Marker(mapView).apply {
                        position = location
                        title = "Your Location"
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                )
            }

            //draw path on map using the list of points currently in the GPS view model
            pathOverlay.setPoints(locationViewModel.pathPoints)

            mapView.invalidate()
        },modifier = modifier
    )
}
