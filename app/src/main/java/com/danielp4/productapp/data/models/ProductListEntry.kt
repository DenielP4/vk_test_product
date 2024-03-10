package com.danielp4.productapp.data.models

data class ProductListEntry(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: String,
    val price: Int,
    val brand: String,
)
