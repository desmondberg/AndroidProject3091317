package com.griffith.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.griffith.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(isDark: Boolean, onThemeChange: (Boolean) -> Unit,
                   settingsViewModel: SettingsViewModel
) {
    Scaffold (
        topBar = { TopAppBar(title = { Text("Settings") }) }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Dark Mode")
                Switch(
                    checked = isDark,
                    onCheckedChange = { onThemeChange(it) }
                )

            }
            Row {
                Button(
                    onClick = { settingsViewModel.isImperial = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!settingsViewModel.isImperial) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text("Metric")
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Imperial button
                Button(
                    onClick = { settingsViewModel.isImperial = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (settingsViewModel.isImperial) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text("Imperial")
                }
            }
        }
    }
}
