package ru.gb.m13_databinding

import android.util.LayoutDirection
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

// logd -> TAB
private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    // Объявляем переменную для хранения корутины поиска
    private lateinit var searchJob: Job

    private val _state: MutableStateFlow<State> = MutableStateFlow<State>(State.Start)
    val state: StateFlow<State> = _state.asStateFlow()

    val searchString = MutableStateFlow("")

    fun startSearch(v: View) {
        Log.d(TAG, "СТАРТ ПОИСКА")
        searchString
            .debounce(300)
            .filter { it.length > 2 }
            .distinctUntilChanged()
            .onEach { request ->
                Log.d(TAG, "Получение из View: $request = ${request.length}")

                // Отменяем предыдущую корутину, если она была запущена - (*) читай сноску
                if (::searchJob.isInitialized && searchJob.isActive) {
                    Log.d(TAG, "предыдущая корутина остановлена")
                    searchJob.cancel()
                }

                // Запускаем новую корутину
                searchJob = viewModelScope.launch {
                    Log.d(TAG, "запуск новой корутины... ")
                    _state.value = State.Loading()
                    delay(5_000)
                    _state.value = State.Fail(request)
                    Log.d(TAG, "request = $request result ${_state.value.result}")
                }
            }.launchIn(viewModelScope)
    }


}


/*
(*)
Знак :: в данном контексте используется для ссылки на член класса или функции верхнего уровня.

В строке кода ::searchJob мы используем его для обращения к свойству или методу класса.
Здесь searchJob - это свойство класса SearchViewModel, и мы используем ::searchJob для обращения
к этому свойству без необходимости создавать экземпляр класса SearchViewModel.

Таким образом, ::searchJob.isInitialized проверяет, инициализировано ли свойство searchJob,
и searchJob.isActive проверяет, активна ли текущая корутина searchJob.
 */