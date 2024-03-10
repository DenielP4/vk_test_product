package com.danielp4.productapp.util

sealed class UiEvent{

    data class Navigate(val route: String): UiEvent()

}