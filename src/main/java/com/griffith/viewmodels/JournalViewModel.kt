package com.griffith.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.griffith.models.JournalItem

//keeps track of journal entries, will be connected to the database later
class JournalViewModel : ViewModel() {
    private val _journalItems = mutableStateListOf<JournalItem>()
    val journalItems: List<JournalItem> get() = _journalItems
    //add an entry
    fun addEntry(item: JournalItem) {
        _journalItems.add(item)
    }
    //remove an entry
    fun removeEntry(id: String) {
        _journalItems.removeIf { it.id == id }
    }
}
