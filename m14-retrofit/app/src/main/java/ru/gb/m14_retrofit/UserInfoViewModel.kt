package ru.gb.m14_retrofit

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserInfoViewModel : ViewModel() {

    // поле для dataBinding - состояние
    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Success)
    val state: StateFlow<State> = _state.asStateFlow()

    // поле для dataBinding - пользователь
    private val _userLiveData = MutableLiveData<User>()
    val userLiveData: MutableLiveData<User> get() = _userLiveData

    fun loadUserInfo(context: Context) {
        viewModelScope.launch {
            try {
                _state.value = State.Loading
                val response = RetrofitInstance.getUserAPI.getUser()
                _userLiveData.postValue(response.results.first())
                _state.value = State.Success
            } catch (e: Exception) {
                _state.value = State.Fail
                Log.e("ViewModel Error", "Failed to get user info", e)
                Toast.makeText(context, "Ошибка в данных. Загрузка не удалась.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}