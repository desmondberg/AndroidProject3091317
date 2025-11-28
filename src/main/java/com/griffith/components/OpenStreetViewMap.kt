package com.griffith.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun OpenStreetViewMap(modifier: Modifier=Modifier){
    val context = LocalContext.current
    //testing geopoint
    var point by remember{mutableStateOf(GeoPoint(53.33,-6.28))}
    //user location state
    var currentLocation by remember{mutableStateOf<GeoPoint?>(null)}

    //OpenStreetMap configuration
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))


    AndroidView(factory = { context ->
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            //allow multi touch
            setMultiTouchControls(true)

            //create a marker
            val marker = Marker(this).apply {
                position = point
                title = "Test"
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }
            this.overlays.add(marker)


            //zoom to Dublin by default
            controller.setZoom(16.0)
            controller.setCenter(GeoPoint(53.33, -6.28))
        }
    }, update={ view ->

    },modifier = modifier)
}