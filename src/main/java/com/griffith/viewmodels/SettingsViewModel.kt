package com.griffith.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    //global settings
    //units - metric or imperial
    var isImperial: Boolean by mutableStateOf(false)
}