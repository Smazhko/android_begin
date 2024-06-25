package ru.gb.m17_recyclerview.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gb.m17_recyclerview.data.PhotoDTO
import ru.gb.m17_recyclerview.useCase.GetNasaPhotoUseCase
import javax.inject.Inject

val TAG = ">>>>> VIEW MODEL"
@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val useCase: GetNasaPhotoUseCase
) : ViewModel() {

    private val _photosStateFlow = MutableStateFlow<List<PhotoDTO>>(emptyList())
    val photosStateFlow = _photosStateFlow.asStateFlow()

    init {
        Log.d(TAG, "INIT")
        loadPhotos()
    }

    private fun loadPhotos(){
        viewModelScope.launch (Dispatchers.IO){
            Log.d(TAG, "kotlin.runCatching")
            kotlin.runCatching {
                useCase.execute()
            }.fold(
                onSuccess = {
                    _photosStateFlow.value = it
                    Log.d(TAG, "LIST SIZE = " + it.size.toString())},
                onFailure = { Log.d(TAG, it.message ?: "")}
            )
        }
    }

}