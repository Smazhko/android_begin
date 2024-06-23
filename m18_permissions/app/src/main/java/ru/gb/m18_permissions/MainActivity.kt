package ru.gb.m18_permissions

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.gb.m18_permissions.databinding.ActivityMainBinding
import ru.gb.m18_permissions.viewmodel.PhotoViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val photoViewModel: PhotoViewModel by viewModels()
    private lateinit var adapter: PhotoAdapter

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            navigateToAddPhotoActivity()
        } else {
            showPermissionDeniedMessage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = photoViewModel
        binding.lifecycleOwner = this

        setupRecyclerView()
        setupFab()

        photoViewModel.allPhotos.observe(this, Observer { photos ->
            photos?.let { adapter.setPhotos(it) }
        })
    }

    private fun setupRecyclerView() {
        adapter = PhotoAdapter()
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.adapter = adapter
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                navigateToAddPhotoActivity()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun navigateToAddPhotoActivity() {
        val intent = Intent(this, AddPhotoActivity::class.java)
        startActivity(intent)
    }

    private fun showPermissionDeniedMessage() {
        Snackbar.make(binding.root, "Приложение не будет работать без разрешения на использование камеры.", Snackbar.LENGTH_LONG)
            .setAction("Разрешить") {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            .show()
    }
}