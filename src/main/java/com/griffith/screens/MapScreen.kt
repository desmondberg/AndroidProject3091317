package com.griffith.screens

import JourneyTracker
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.griffith.components.OpenStreetViewMap
import com.griffith.viewmodels.SettingsViewModel
import com.griffith.viewmodels.StopwatchViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MapScreen(stopwatchViewModel: StopwatchViewModel,settingsViewModel: SettingsViewModel) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Map") }) },
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            OpenStreetViewMap(
                modifier = Modifier.fillMaxSize()
            )
            JourneyTracker(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 96.dp
                    )
                    .shadow(4.dp, shape = RoundedCornerShape(16.dp)),
                stopwatchViewModel = stopwatchViewModel,
                settingsViewModel = settingsViewModel
            )
        }
    }
}

