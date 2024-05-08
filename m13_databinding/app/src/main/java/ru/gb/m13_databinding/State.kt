package ru.gb.m13_databinding

sealed class State(
    val isLoading: Boolean = false,
    open val request: String? = null,
    open val result: String = "Тут будут результаты поиска...",
) {
    data object Start: State()

    data class Loading(override val result: String = "Идёт поиск. Подождите...") : State(isLoading = true)

    data class Success(
        override val request: String?,
        override val result: String = "Поиск \"$request\" УСПЕШЕН!") : State(
        request = request
        )

    data class Fail(
        override val request: String?,
        override val result: String = "По запросу \"$request\" ничего не найдено.") : State(
        request = request
        )
}