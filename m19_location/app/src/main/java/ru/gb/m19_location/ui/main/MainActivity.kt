package ru.gb.m19_location.ui.main

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import ru.gb.m19_location.R
import ru.gb.m19_location.databinding.ActivityMainBinding
import ru.gb.m19_location.databinding.ActivityInfoWindowBinding
import ru.gb.m19_location.ui.main.models.POI

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var map: MapView
    private val viewModel: MainViewModel by viewModels()
    private var currentLocationMarker: Marker? = null
    private var savedCenter: GeoPoint? = null
    private var savedZoom: Double? = null
    private val poiMarkers = mutableListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, androidx.preference.PreferenceManager.getDefaultSharedPreferences(this))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        map = binding.map
        map.setMultiTouchControls(true)
        map.zoomController.setVisibility(org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER)

        if (savedInstanceState != null) {
            savedCenter = GeoPoint(savedInstanceState.getDouble("lat"), savedInstanceState.getDouble("lon"))
            savedZoom = savedInstanceState.getDouble("zoom")
        }

        binding.btnZoomIn.setOnClickListener { map.controller.zoomIn() }
        binding.btnZoomOut.setOnClickListener { map.controller.zoomOut() }
        binding.btnCurrentLocation.setOnClickListener {
            if (checkLocationPermissions()) {
                viewModel.updateCurrentLocation(true)
            } else {
                requestLocationPermissions()
            }
        }

        viewModel.currentLocation.observe(this, Observer { location ->
            location?.let {
                val geoPoint = GeoPoint(it.latitude, it.longitude)
                updateCurrentLocationMarker(geoPoint)
                if (savedCenter == null && savedZoom == null) {
                    map.controller.setCenter(geoPoint)
                    map.controller.setZoom(15.0)
                }
            }
        })

        viewModel.forceCenter.observe(this, Observer { forceCenter ->
            if (forceCenter) {
                viewModel.currentLocation.value?.let {
                    val geoPoint = GeoPoint(it.latitude, it.longitude)
                    map.controller.setCenter(geoPoint)
                    map.controller.setZoom(15.0)
                    viewModel.clearForceCenter()
                }
            }
        })

        viewModel.locationError.observe(this, Observer { isError ->
            if (isError) {
                Snackbar.make(binding.root, "Невозможно определить местоположение пользователя", Snackbar.LENGTH_LONG).show()
                viewModel.clearLocationError()
            }
        })

        viewModel.poiList.observe(this, Observer { pois ->
            map.overlays.removeAll(poiMarkers)
            poiMarkers.clear()
            for (poi in pois) {
                val marker = Marker(map)
                marker.position = GeoPoint(poi.geometry.coordinates[1], poi.geometry.coordinates[0])
                marker.title = poi.properties.name
                marker.setOnMarkerClickListener { marker, mapView ->
                    showInfoWindow(marker, poi.properties)
                    true
                }
                Log.d("MainActivity", "Adding marker: ${poi.properties.name} at ${poi.geometry.coordinates}")
                poiMarkers.add(marker)
                map.overlays.add(marker)
            }
            // Добавляем маркер текущего местоположения обратно на карту
            currentLocationMarker?.let { map.overlays.add(it) }
            map.invalidate() // Обновляем карту
        })

        if (!checkLocationPermissions()) {
            requestLocationPermissions()
        } else {
            if (savedCenter == null && savedZoom == null) {
                viewModel.updateCurrentLocation()
            }
        }

        if (savedCenter != null && savedZoom != null) {
            map.controller.setCenter(savedCenter)
            map.controller.setZoom(savedZoom!!)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val center = map.mapCenter as GeoPoint
        outState.putDouble("lat", center.latitude)
        outState.putDouble("lon", center.longitude)
        outState.putDouble("zoom", map.zoomLevelDouble)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.updateCurrentLocation()
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }

    private fun updateCurrentLocationMarker(geoPoint: GeoPoint) {
        if (currentLocationMarker == null) {
            currentLocationMarker = Marker(map)
            currentLocationMarker?.icon = resources.getDrawable(R.drawable.ic_location_marker, theme)
            currentLocationMarker?.title = "Я здесь"
        }
        currentLocationMarker?.position = geoPoint
        currentLocationMarker?.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map.overlays.add(currentLocationMarker)
        map.invalidate() // Обновляем карту
    }

    private fun showInfoWindow(marker: Marker, properties: POI.Properties) {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val binding = ActivityInfoWindowBinding.inflate(inflater)
        builder.setView(binding.root)

        val dialog = builder.create()
        binding.infoTitle.text = properties.name
        binding.infoDescription.text = "Description: ${properties.kinds}" // Заполняем описание

        binding.closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
