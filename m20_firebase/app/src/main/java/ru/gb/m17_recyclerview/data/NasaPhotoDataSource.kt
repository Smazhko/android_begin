package ru.gb.m17_recyclerview.data

import javax.inject.Inject

class NasaPhotoDataSource @Inject constructor() {
    suspend fun loadPhotoList(): List<PhotoDTO>{
        return RetrofitInstance.getNasaPhotoAPI.getPhotoList().photos
    }
}