package ru.gb.m18_permissions

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import ru.gb.m18_permissions.databinding.ActivityAddPhotoBinding
import ru.gb.m18_permissions.model.Photo
import ru.gb.m18_permissions.viewmodel.PhotoViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPhotoBinding
    private val photoViewModel: PhotoViewModel by viewModels()
    private lateinit var currentPhotoPath: String

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            val photo = Photo(filePath = currentPhotoPath, date = System.currentTimeMillis())
            photoViewModel.insert(photo)
            finish() // возвращаемся к MainActivity после добавления фото
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = photoViewModel
        binding.lifecycleOwner = this

        binding.takePhotoButton.setOnClickListener {
            val photoFile = createImageFile()
            val photoURI = FileProvider.getUriForFile(
                this,
                "ru.gb.m18_permissions.fileprovider",
                photoFile
            )
            takePictureLauncher.launch(photoURI)
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
}