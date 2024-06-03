package ru.gb.m17_recyclerview.useCase

import android.util.Log
import ru.gb.m17_recyclerview.data.NasaPhotoRepository
import ru.gb.m17_recyclerview.data.PhotoDTO
import javax.inject.Inject

class GetNasaPhotoUseCase @Inject constructor(
    private val nasaPhotoRepository: NasaPhotoRepository
) {

    suspend fun execute(): List<PhotoDTO> {
        val list = nasaPhotoRepository.getPhotoList()
        Log.d("USE CASE", list.size.toString())
        return list
    }
}