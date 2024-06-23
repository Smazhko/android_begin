package ru.gb.m18_permissions.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ru.gb.m18_permissions.database.PhotoDatabase
import ru.gb.m18_permissions.model.Photo
import ru.gb.m18_permissions.repository.PhotoRepository
import kotlinx.coroutines.launch

class PhotoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PhotoRepository
    val allPhotos: LiveData<List<Photo>>

    init {
        val photoDao = PhotoDatabase.getDatabase(application).photoDao()
        repository = PhotoRepository(photoDao)
        allPhotos = repository.allPhotos
    }

    fun insert(photo: Photo) = viewModelScope.launch {
        repository.insert(photo)
    }
}