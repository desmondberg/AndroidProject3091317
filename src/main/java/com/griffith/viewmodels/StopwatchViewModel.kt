package com.griffith.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//a view model to store the state of the stopwatch
class StopwatchViewModel : ViewModel() {
    private var isRunning by mutableStateOf(false);
    var elapsedTimeInMilliseconds by mutableLongStateOf(0L)
        private set
    private var updateTime: Job? = null


    //start function, sets isRunning to true, then runs a coroutine that ticks the stopwatch
    fun start(){
        if(isRunning){
            return
        }
        isRunning=true
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
        updateTime?.cancel()
        if(!isPause){
            elapsedTimeInMilliseconds=0L
        }
    }
}