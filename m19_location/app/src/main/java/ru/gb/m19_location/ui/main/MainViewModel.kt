package ru.gb.m19_location.ui.main

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import ru.gb.m19_location.ui.main.models.POI

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)

    private val _currentLocation = MutableLiveData<Location?>()
    val currentLocation: LiveData<Location?> get() = _currentLocation

    private val _poiList = MutableLiveData<List<POI>>()
    val poiList: LiveData<List<POI>> get() = _poiList

    private val _locationError = MutableLiveData<Boolean>()
    val locationError: LiveData<Boolean> get() = _locationError

    private val _forceCenter = MutableLiveData<Boolean>()
    val forceCenter: LiveData<Boolean> get() = _forceCenter

    private var savedLocation: Location? = null

    fun updateCurrentLocation(force: Boolean = false) {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    _currentLocation.postValue(location)
                    savedLocation = location
                    fetchPOIs(location)
                    if (force) {
                        _forceCenter.postValue(true)
                    }
                } else {
                    _locationError.postValue(true)
                    _currentLocation.postValue(savedLocation)
                    savedLocation?.let { fetchPOIs(it) }
                }
            }
        } else {
            _locationError.postValue(true)
            _currentLocation.postValue(savedLocation)
            savedLocation?.let { fetchPOIs(it) }
        }
    }

    fun clearLocationError() {
        _locationError.postValue(false)
    }

    fun clearForceCenter() {
        _forceCenter.postValue(false)
    }

    private fun fetchPOIs(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val lonMin = location.longitude - 0.1
                val latMin = location.latitude - 0.1
                val lonMax = location.longitude + 0.1
                val latMax = location.latitude + 0.1
                val url = "https://api.opentripmap.com/0.1/ru/places/bbox?lon_min=$lonMin&lat_min=$latMin&lon_max=$lonMax&lat_max=$latMax&kinds=interesting_places&format=geojson&apikey=5ae2e3f221c38a28845f05b6a7c19433a40f3b37f414e97740d98108"
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(url)
                    .build()
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                val json = JSONObject(responseBody ?: "")
                val pois = mutableListOf<POI>()
                val features = json.getJSONArray("features")
                for (i in 0 until features.length()) {
                    val feature = features.getJSONObject(i)
                    val geometry = feature.getJSONObject("geometry")
                    val properties = feature.getJSONObject("properties")
                    val coordinates = geometry.getJSONArray("coordinates")
                    val poi = POI(
                        type = feature.getString("type"),
                        geometry = POI.Geometry(
                            type = geometry.getString("type"),
                            coordinates = listOf(coordinates.getDouble(0), coordinates.getDouble(1))
                        ),
                        properties = POI.Properties(
                            xid = properties.getString("xid"),
                            name = properties.getString("name"),
                            rate = properties.getInt("rate"),
                            osm = properties.optString("osm", ""),
                            wikidata = properties.optString("wikidata", ""),
                            kinds = properties.getString("kinds")
                        )
                    )
                    pois.add(poi)
                }
                _poiList.postValue(pois)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
