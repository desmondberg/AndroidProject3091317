package com.griffith.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.griffith.components.OpenStreetViewMap

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MapScreen(){
    Scaffold(
        topBar = { TopAppBar(title = { Text("Map") }) }
    ) {  paddingValues ->
        Box(){
            OpenStreetViewMap()
        }
    }
}

//The journey tracker pops up from the bottom when the user starts a journey. The tracker consists of a stopwatch,
//information about distance travelled so far, current speed and average speed (either in km/h or mph depending on user settings)
@Composable
fun JourneyTracker(){

}

@Composable
fun JourneyStopwatch(){

}