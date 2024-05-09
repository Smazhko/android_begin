package ru.gb.m13_databinding

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

// logd -> TAB
private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    // Объявляем переменную для хранения корутины поиска
    private lateinit var _searchJob: Job

    private val _state: MutableStateFlow<State> = MutableStateFlow<State>(State.Start)
    val state: StateFlow<State> = _state.asStateFlow()

    val searchString = MutableStateFlow("")

    init {
        viewModelScope.launch {
            searchString
                .debounce(300)
                .filter { it.length > 2 }
                .onEach {
                    startSearch(it)
                }.collect()
        }
    }

    fun startSearch(request: String) {
        Log.d(TAG, "СТАРТ ПОИСКА")

        Log.d(TAG, "Получение из View: $request (длина ${request.length})")

        // Отменяем предыдущую корутину, если она была запущена - (*) читай сноску
        if (::_searchJob.isInitialized && _searchJob.isActive) {
            _searchJob.cancel()
            Log.d(TAG, "предыдущая корутина остановлена - ${_searchJob.isCancelled}")
        }
        // Запускаем новую корутину
        _searchJob = viewModelScope.launch {
            Log.d(TAG, "запуск новой корутины... ")
            doSearch(request)
        }
    }

    suspend fun doSearch(requestStr: String): Unit {
        _state.value = State.Loading()
        delay(5_000)
        _state.value = State.Fail(requestStr)
        Log.d(TAG, "request = $requestStr result ${_state.value.result}")
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