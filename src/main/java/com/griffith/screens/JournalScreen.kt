package com.griffith.screens

import AddressText
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.griffith.database.JournalItemEntity
import com.griffith.viewmodels.JournalViewModel
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")


@Composable
fun JournalScreen(journalViewModel: JournalViewModel) {
    var showForm by remember { mutableStateOf(false) }
    val sdf = SimpleDateFormat("mm:ss", Locale.getDefault())
    val journalEntries by journalViewModel.journalItems.collectAsState(initial = emptyList())

    Scaffold(
        topBar = { TopAppBar(title = { Text("My Journal") }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(journalEntries) { entry ->
                    JournalEntryCard(entry, journalViewModel)
                }
            }

            Button(
                onClick = { showForm = true },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 90.dp)
                    .fillMaxWidth(0.9f)
                    .height(60.dp)
            ) {
                Text("Add Entry", fontSize = 20.sp)
            }

            if (showForm) {
                var title by remember { mutableStateOf("") }
                var description by remember { mutableStateOf("") }
                var journeyType by remember { mutableStateOf("") }

                BasicAlertDialog(onDismissRequest = { showForm = false }) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text("New Journal", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(24.dp))
                            TextField(
                                label = { Text("Journal Title") },
                                value = title,
                                onValueChange = { title = it },
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            TextField(
                                label = { Text("Description") },
                                value = description,
                                onValueChange = { description = it },
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            TextField(
                                label = { Text("Journey Type") },
                                value = journeyType,
                                onValueChange = { journeyType = it },
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(onClick = { showForm = false }) {
                                    Text("Cancel")
                                }
                                Button(onClick = {
                                    //create a new journal item entity
                                    val newEntry = JournalItemEntity(
                                        title = title,
                                        description = description,
                                        journeyType = journeyType,
                                        startTime = 0L,
                                        endTime = 0L,
                                        startGeoPointLat = null,
                                        startGeoPointLng = null,
                                        endGeoPointLat = null,
                                        endGeoPointLng = null,
                                        distanceTravelled = 0f
                                    )
                                    journalViewModel.addEntry(newEntry)
                                    showForm = false
                                }) {
                                    Text("Add")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
//represents a journal entry on the journal screen
fun JournalEntryCard(
    entry: JournalItemEntity,
    journalViewModel: JournalViewModel
) {
    //modal toggle variables
    var showEditForm by remember { mutableStateOf(false) }
    var showRemoveDialog by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Box {
            Column(modifier = Modifier.padding(8.dp)) {
                //title
                Text(entry.title, style = MaterialTheme.typography.titleMedium)
                //description and journey type
                Row {
                    Text(entry.description, fontStyle = FontStyle.Italic)
                    Spacer(Modifier.width(8.dp))
                    Text(entry.journeyType, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(8.dp))

                //location info
                if (entry.startGeoPointLat != null && entry.startGeoPointLng != null &&
                    entry.endGeoPointLat != null && entry.endGeoPointLng != null
                ) {
                    Row {
                        Text("Started at: ")
                        AddressText(
                            lat = entry.startGeoPointLat,
                            lon = entry.startGeoPointLng,
                            viewModel = journalViewModel
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Row {
                        Text("Ended at: ")
                        AddressText(
                            lat = entry.endGeoPointLat,
                            lon = entry.endGeoPointLng,
                            viewModel = journalViewModel
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                //duration info
                if (entry.startTime > 0 && entry.endTime > 0) {
                    Row {
                        Text("Duration: ${SimpleDateFormat("mm:ss", Locale.getDefault()).format(entry.endTime - entry.startTime)}")
                    }
                }

                Spacer(Modifier.height(8.dp))
                //distance info
                entry.distanceTravelled?.let { distance ->
                    val durationHours = (entry.endTime - entry.startTime)/ 3_600_000f

                    if (distance > 0f && durationHours > 0f) {
                        Row {
                            Text("Distance travelled: %.2f metres".format(distance))
                        }
                        Spacer(Modifier.height(8.dp))
                        Row {
                            Text("Average speed: %.1f km/h".format(distance / durationHours / 1000f))
                        }
                    }
                }

            }

            //corner buttons
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                //edit entry button
                IconButton(onClick = { showEditForm = true }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Entry"
                    )
                }
                //remove entry button
                IconButton(onClick = { showRemoveDialog = true }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Remove Entry"
                    )
                }
            }
        }
    }

    //edit modal
    if (showEditForm) {
        BasicAlertDialog(onDismissRequest = { showEditForm = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Edit Journal", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

        //remove modal
    if (showRemoveDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = { Text("Remove Entry") },
            text = { Text("Are you sure you want to delete this journal entry?") },
            confirmButton = {
                TextButton(onClick = {
                    showRemoveDialog = false
                    //remove entry from the database
                    journalViewModel.removeEntry(entry)
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}

