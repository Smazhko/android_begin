package ru.gb.m16_architecture.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.gb.m16_architecture.data.ToonHeroDTO
import ru.gb.m16_architecture.useCase.GetToonHeroUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getToonHeroUseCase: GetToonHeroUseCase
) : ViewModel() {
    // поле для dataBinding - состояние
    private val _stateLiveData: MutableLiveData<State> = MutableLiveData(State.Success)
    val stateLiveData: LiveData<State> get() = _stateLiveData

    // поле для dataBinding - герой
    private val _heroLiveData = MutableLiveData<ToonHeroDTO>()
    val heroLiveData: LiveData<ToonHeroDTO> get() = _heroLiveData

    private var _initialLoad = true

    fun loadHeroInfo() {
        viewModelScope.launch {
            try {
                _stateLiveData.value = State.Loading
                val newHero = getToonHeroUseCase.execute()
                _heroLiveData.postValue(newHero)
                _stateLiveData.value = State.Success
            } catch (e: Exception) {
                _stateLiveData.value = State.Fail
                Log.e("ViewModel Error", "Failed to get toon hero info", e)
            }
        }
    }

    // метод для первичной загрузки данных
    // нужен для того, чтобы данные не обновлялись каждый раз
    // при перевороте телефона - каждый раз вызывается onViewCreated
    fun loadInitialHeroInfo() {
        if (_initialLoad) {
            loadHeroInfo()
            _initialLoad = false
        }
    }
}