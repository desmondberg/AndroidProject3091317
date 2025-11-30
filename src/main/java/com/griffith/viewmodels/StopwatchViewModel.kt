package com.griffith.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.Instant

//a view model to store the state of the stopwatch
class StopwatchViewModel : ViewModel() {
    var isRunning by mutableStateOf(false)
        private set
    var isPaused by mutableStateOf(false)
        private set
    var elapsedTimeInMilliseconds by mutableLongStateOf(0L)
        private set

    var startMillis by mutableLongStateOf(0L)
        private set
    var stopMillis by mutableLongStateOf(0L)
        private set

    private var updateTime: Job? = null
    private val _stopwatchFlow = MutableSharedFlow<String>()
    val stopwatchFlow: SharedFlow<String> = _stopwatchFlow


    //start function, sets isRunning to true, then runs a coroutine that ticks the stopwatch
    fun start(){
        if(isRunning){
            return
        }
        //only update startMillis on the first time the stopwatch is started
        if(startMillis==0L){
            startMillis = System.currentTimeMillis()
        }
        isRunning=true
        isPaused=false
        updateTime = viewModelScope.launch {
            val startTime = System.currentTimeMillis() - elapsedTimeInMilliseconds

            while (isRunning) {
                elapsedTimeInMilliseconds = System.currentTimeMillis() - startTime
                delay(50)
            }
        }

    }
    //stop function, either pauses the stopwatch at its current time or resets it completely depending on if isPuase is true or not
    fun stop(isPause: Boolean=false){
        isRunning=false
        stopMillis = System.currentTimeMillis()
        updateTime?.cancel()
        if(isPause){
            isPaused=true
        }else{
            elapsedTimeInMilliseconds=0L
            viewModelScope.launch {
                _stopwatchFlow.emit("Stopwatch ended")
            }
        }
    }
}