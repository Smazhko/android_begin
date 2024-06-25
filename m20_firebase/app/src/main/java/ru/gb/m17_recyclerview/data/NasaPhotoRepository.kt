package ru.gb.m17_recyclerview.data

import javax.inject.Inject

class NasaPhotoRepository @Inject constructor(private val photoDataSource: NasaPhotoDataSource) {
    suspend fun getPhotoList(): List<PhotoDTO> {
        return photoDataSource.loadPhotoList()
    }
}