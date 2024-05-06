package ru.gb.m12_mvvm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// logt -> TAB
private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    private val _state : MutableStateFlow<State> = MutableStateFlow<State>(State.Start)
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        Log.d(TAG, "init: ")
    }
    
    fun onButtonClick() {
        Log.d(TAG, "onButtonClick: ")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: ")
    }

    fun onSignInClick(searchString: String) {
        viewModelScope.launch {
            _state.value = State.Loading
            delay(7_000)
            _state.value = State.Success
        }
    }
}