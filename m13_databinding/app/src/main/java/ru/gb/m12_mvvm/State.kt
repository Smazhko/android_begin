package ru.gb.m12_mvvm

sealed class State {
    object Loading : State()
    object Success : State()
}