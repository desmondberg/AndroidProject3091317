package com.griffith.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.griffith.database.JournalItemDatabase
import com.griffith.database.JournalItemEntity
import com.griffith.models.JournalItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

//keeps track of journal entries
class JournalViewModel(application: Application) :
    AndroidViewModel(application) {

    private val journalItem =
        JournalItemDatabase
            .getInstance(application)
            .journalItemDao()

    val journalItems: Flow<List<JournalItemEntity>> =
        journalItem.getJournalItems()

    fun addEntry(item: JournalItemEntity) {
        viewModelScope.launch {
            journalItem.insert(item)
        }
    }

    fun removeEntry(item: JournalItemEntity) {
        viewModelScope.launch {
            journalItem.delete(item)
        }
    }

    //reverse geocoding

    fun reverseGeocode(lat: Double, lng: Double): String? {
        val urlStr = "https://nominatim.openstreetmap.org/reverse?lat=$lat&lon=$lng&format=json"
        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection
        //requests to nominatim require a user agent header
        conn.setRequestProperty("User-Agent", "Journey Tracker")
        return try {
            val stream = BufferedReader(InputStreamReader(conn.inputStream))
            val res = stream.use { it.readText() }
            val json = JSONObject(res)
            json.optString("display_name", null) }
        catch (e: Exception) {
            e.printStackTrace()
            null
        } finally { conn.disconnect() }

    }


    //store reversed geocoded addresses here to prevent unnecessary requests
    private val reversedGeocodedAddresses = mutableMapOf<Pair<Double, Double>, String>()
    //get an existing reverse geocoded address, or send a request if there isn't one
    fun fetchAddress(lat: Double, lon: Double): String? {
        val key = lat to lon
        if (reversedGeocodedAddresses.containsKey(key)) return reversedGeocodedAddresses[key]

        viewModelScope.launch(Dispatchers.IO) {
            reversedGeocodedAddresses[key] = reverseGeocode(lat, lon) ?: "N/A"
        }
        return reversedGeocodedAddresses[key]
    }

}
