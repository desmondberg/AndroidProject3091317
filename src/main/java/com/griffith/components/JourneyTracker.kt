import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import com.griffith.components.linearAccelerationSensor
import com.griffith.models.JournalItem
import com.griffith.viewmodels.GPSLocationViewModel
import com.griffith.viewmodels.JournalViewModel
import com.griffith.viewmodels.SettingsViewModel
import com.griffith.viewmodels.StopwatchViewModel
import kotlin.math.pow
import kotlin.math.sqrt


//this component serves as the info panel on the map screen that lets the user start the stopwatch, and displays their journey info
@Composable
fun JourneyTracker(
    modifier: Modifier = Modifier,
    //view models
    stopwatchViewModel: StopwatchViewModel,
    settingsViewModel: SettingsViewModel,
    journalViewModel: JournalViewModel,
    locationViewModel: GPSLocationViewModel
) {
    //get context for toast
    val context = LocalContext.current

    //elapsed time in seconds
    val elapsedTime by remember {
        derivedStateOf { stopwatchViewModel.elapsedTimeInMilliseconds / 1000 }
    }

    //get the raw linear acceleration data from the sensor (three axes)
    val lAccel = linearAccelerationSensor()
    //calculate magnitude from all axes to get an "average"
    val magnitude = remember(lAccel) {
        sqrt(
            lAccel.x*lAccel.x +
                    lAccel.y*lAccel.y +
                    lAccel.z*lAccel.z
        )
    }

    //display units in either metres and kilometres or feet and miles
    val metresOrFeet = if(settingsViewModel.isImperial) "ft" else "m"
    val kilometresOrMiles = if(settingsViewModel.isImperial) "mi" else "km"

    //create a new journal entry when stopwatch is stopped
    LaunchedEffect(Unit) {
        stopwatchViewModel.stopwatchFlow.collect { message ->
            if (message == "Stopwatch ended") {
                val newJournalEntry = JournalItem(
                    title = "New Journey",
                    description = "Description here",
                    journeyType = "Journey",
                    startTime = stopwatchViewModel.startMillis,
                    endTime = stopwatchViewModel.stopMillis,
                    //for now, both start and end positions are the same
                    startPosition = locationViewModel.startLocation.value,
                    endPosition = locationViewModel.endLocation.value
                )
                //add new entry to journal view model
                journalViewModel.addEntry(newJournalEntry)
                //display toast
                Toast.makeText(context, "Journal entry added", Toast.LENGTH_SHORT).show()

            }
        }
    }

    Column(
        modifier = modifier
            //rounded corners
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(16.dp),
        //position it in the middle
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //information dasboard - time, speed, distance
        Column {
            Row {
                Text("Time: ${elapsedTime}s")
                Spacer(modifier = Modifier.width(12.dp))
                Text("Speed: ${if(stopwatchViewModel.isRunning) "%.2f".format(if(settingsViewModel.isImperial) magnitude*3.28 else magnitude) else "" } $metresOrFeet/s^2")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Row {
                Text("Distance travelled: ${"N/A" /*if(settingsViewModel.isImperial) distance*0.621 else distance*/} $kilometresOrMiles")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // start/stop and resume/pause stopwatch buttons
        Row {
            //start/stop
            Button(
                onClick = {
                    if(stopwatchViewModel.isPaused){
                        //do nothing
                    }else{
                        if(stopwatchViewModel.isRunning){
                            locationViewModel.endLocation.value = locationViewModel.currentLocation.value
                            stopwatchViewModel.stop()
                        }else {
                            locationViewModel.startLocation.value = locationViewModel.currentLocation.value
                            stopwatchViewModel.start()
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                //grey-out the button if the stopwatch is currently paused
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (stopwatchViewModel.isPaused) MaterialTheme.colorScheme.tertiary
                    else MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if(stopwatchViewModel.isRunning){
                    Text("Stop")
                }else{
                    Text("Start")
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // resume/pause
            Button(
                onClick = { if(stopwatchViewModel.isPaused){
                    stopwatchViewModel.start()
                }else{
                    stopwatchViewModel.stop(isPause = true)
                }},
                modifier = Modifier.weight(1f)
            ) {
                if(stopwatchViewModel.isRunning){
                    Text("Pause")
                }else{
                    Text("Resume")
                }
            }
        }
    }
}
