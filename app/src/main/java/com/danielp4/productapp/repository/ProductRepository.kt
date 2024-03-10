package com.danielp4.productapp.repository

import com.danielp4.productapp.data.remote.ProductApi
import com.danielp4.productapp.data.remote.responses.Product
import com.danielp4.productapp.data.remote.responses.ProductList
import com.danielp4.productapp.util.Resource

class ProductRepository(
    private val api: ProductApi
) {

    suspend fun getProductList(skip: Int, limit: Int): Resource<ProductList> {
        val response = try {
            api.getProductList(skip, limit)
        } catch(e: Exception) {
            return Resource.Error("Проверьте подключение к интернету!")
        }
        return Resource.Success(response)
    }

    suspend fun getProductInfo(idProduct: Int): Resource<Product> {
        val response = try {
            api.getProductInfo(idProduct)
        } catch(e: Exception) {
            return Resource.Error("Проверьте подключение к интернету!")
        }
        return Resource.Success(response)
    }

}