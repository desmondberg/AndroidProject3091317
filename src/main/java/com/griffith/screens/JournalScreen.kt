package com.griffith.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.griffith.models.JournalItem

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun JournalScreen() {
    var showForm by remember { mutableStateOf(false) }
    val journalEntries = remember { mutableStateListOf<JournalItem>() }

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
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(entry.title, style = MaterialTheme.typography.titleMedium)
                            Row {
                                Text(entry.description, fontStyle = FontStyle.Italic)
                                Spacer(Modifier.width(8.dp))
                                Text(entry.journeyType, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
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

                BasicAlertDialog(onDismissRequest = { showForm = false }, modifier=Modifier.padding(16.dp)) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("New Journal", style = MaterialTheme.typography.titleLarge)
                            TextField(
                                label = { Text("Journal Title") },
                                value = title,
                                onValueChange = { title = it },
                                singleLine = true
                            )
                            TextField(
                                label = { Text("Description") },
                                value = description,
                                onValueChange = { description = it },
                                singleLine = true
                            )
                            TextField(
                                label = { Text("Journey Type") },
                                value = journeyType,
                                onValueChange = { journeyType = it },
                                singleLine = true
                            )
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
                                    journalEntries.add(JournalItem(title, description, journeyType))
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
