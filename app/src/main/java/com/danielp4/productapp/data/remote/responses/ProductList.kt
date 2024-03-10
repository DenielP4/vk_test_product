package com.danielp4.productapp.data.remote.responses

data class ProductList(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)