package com.griffith

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    var buttonText = mutableStateOf("On");
    fun switchText(){
        if(buttonText.value == "On"){
            buttonText.value="Off"
        }else{
            buttonText.value = "On"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Button(onClick={switchText()}) {
                Text(buttonText.value)
            }
        }
    }
}
