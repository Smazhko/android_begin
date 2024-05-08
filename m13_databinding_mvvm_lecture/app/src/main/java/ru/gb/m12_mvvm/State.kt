package ru.gb.m12_mvvm

sealed class State(
    val isLoading: Boolean = false,
    open val loginError: String? = null,
    open val passwordError: String? = null
) {
    object Loading : State(isLoading = true)
    object Success : State()
    data class Error(
        override val loginError: String?,
        override val passwordError: String?
    ) : State(
        loginError = loginError,
        passwordError = passwordError
    )
}