package com.danielp4.productapp.presentation.productlist

sealed class ProductListEvent {
    data class OnProductClick(val route: String): ProductListEvent()
    data class OnTextSearchChange(val text: String): ProductListEvent()
    object OnClickDeleteSearchText: ProductListEvent()
}