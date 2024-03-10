package com.danielp4.productapp.data.remote

import com.danielp4.productapp.data.remote.responses.Product
import com.danielp4.productapp.data.remote.responses.ProductList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @GET("products")
    suspend fun getProductList(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): ProductList

    @GET("products/{id}")
    suspend fun getProductInfo(
        @Path("id") id: Int
    ): Product

}