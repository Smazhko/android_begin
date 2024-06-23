package ru.gb.m18_permissions.repository

import androidx.lifecycle.LiveData
import ru.gb.m18_permissions.dao.PhotoDao
import ru.gb.m18_permissions.model.Photo

class PhotoRepository(private val photoDao: PhotoDao) {

    val allPhotos: LiveData<List<Photo>> = photoDao.getAllPhotos()

    suspend fun insert(photo: Photo) {
        photoDao.insert(photo)
    }
}