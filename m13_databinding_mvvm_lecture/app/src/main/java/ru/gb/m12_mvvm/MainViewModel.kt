package ru.gb.m12_mvvm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

// logt -> TAB
private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    private val _state : MutableStateFlow<State> = MutableStateFlow<State>(State.Success)
    val state: StateFlow<State> = _state.asStateFlow()  // **  СНОСКА - ЧИТАТЬ НИЖЕ

    private val _error = Channel<String>()
    val error: Flow<String> = _error.receiveAsFlow()

    private val _credentials : MutableStateFlow<Credentials> = MutableStateFlow(Credentials())
    val credentials: StateFlow<Credentials> = _credentials.asStateFlow()

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

    fun onSignInClick() {
        viewModelScope.launch {
            val login = credentials.value.login
            val password = credentials.value.password
            val isLoginEmpty = login.isBlank()
            val isPasswordEmpty = password.isBlank()

            if (login.isBlank() || password.isBlank()){
                var loginError: String? = null
                if (isLoginEmpty) {
                    loginError = "Login is  required"
                }
                var passwordError: String? = null
                if (isPasswordEmpty) {
                    passwordError = "Password is  required"
                }
                _state.value = State.Error(loginError, passwordError)
                _error.send("Login or Password isn't valid")
            } else {
                _state.value = State.Loading
                delay(5_000)
                _state.value = State.Success
            }
        }
    }
}

/*

(**)

Использование символа подчеркивания в начале имени переменной является простым способом
обозначить, что переменная предназначена для внутреннего использования внутри класса,
а не для экспорта в публичный интерфейс класса. Это помогает разработчикам лучше понимать,
какие переменные предназначены для использования внутри класса, а какие - для внешнего
взаимодействия с классом.

Такой подход также помогает предотвратить конфликты имен между внутренними переменными
класса и внешними переменными, которые могут быть определены в других частях программы.

В данном коде переменные _state и state используются для различных целей:

_state: Это приватное поле класса MainViewModel, предназначенное для внутреннего
использования в самом классе. Оно содержит изменяемое состояние (MutableStateFlow<State>),
которое используется внутри ViewModel для отслеживания состояния приложения.

state: Это публичное свойство класса MainViewModel, предоставляющее доступ к состоянию
приложения извне класса. Оно является только для чтения (StateFlow<State>), что означает,
что его значение можно получить, но нельзя изменить извне класса. Это позволяет другим
частям приложения наблюдать за состоянием ViewModel, но не изменять его напрямую.

Использование двух разных переменных для хранения внутреннего и публичного представления
одних и тех же данных позволяет явно разграничить внутренние и внешние интерфейсы класса,
что делает код более чистым и понятным.
 */