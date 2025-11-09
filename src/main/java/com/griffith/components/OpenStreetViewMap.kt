package com.griffith.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun OpenStreetViewMap(modifier: Modifier=Modifier){
    val context = LocalContext.current

    //OpenStreetMap configuration
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))

    AndroidView(factory = { context ->
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            //allow multi touch
            setMultiTouchControls(true)
            //zoom to Dublin by default
            controller.setZoom(16.0)
            controller.setCenter(GeoPoint(53.33, -6.28))
        }
    }, modifier = modifier)
}