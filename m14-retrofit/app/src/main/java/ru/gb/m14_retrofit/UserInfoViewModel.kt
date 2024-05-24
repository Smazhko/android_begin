package ru.gb.m14_retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserInfoViewModel : ViewModel() {

    // поле для dataBinding - состояние
    private val _state: MutableLiveData<State> = MutableLiveData(State.Success)
    val state: LiveData<State> get() = _state

    // поле для dataBinding - пользователь
    private val _userLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> get() = _userLiveData

    private var _initialLoad = true

    fun loadUserInfo() {
        viewModelScope.launch {
            try {
                _state.value = State.Loading
                val response = RetrofitInstance.getUserAPI.getUser()
                _userLiveData.postValue(response.results.first())
                _state.value = State.Success
            } catch (e: Exception) {
                _state.value = State.Fail
                Log.e("ViewModel Error", "Failed to get user info", e)
            }
        }
    }

    // метод для первичной загрузки данных
    // нужен для того, чтобы данные не обновлялись каждый раз
    // при перевороте телефона - каждый раз вызывается onViewCreated
    fun loadInitialUserInfo() {
        if (_initialLoad) {
            loadUserInfo()
            _initialLoad = false
        }
    }

}

