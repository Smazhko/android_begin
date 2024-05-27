package ru.gb.m16_architecture.presentation

sealed class State(val isLoading: Boolean = false, val isEnable: Boolean = true) {
    data object Loading : State(isLoading = true, isEnable = false)
    data object Success : State()
    data object Fail : State()
}