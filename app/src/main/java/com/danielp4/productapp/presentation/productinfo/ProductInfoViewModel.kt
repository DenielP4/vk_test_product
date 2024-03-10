package com.danielp4.productapp.presentation.productinfo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielp4.productapp.data.remote.responses.Product
import com.danielp4.productapp.repository.ProductRepository
import com.danielp4.productapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductInfoViewModel @Inject constructor(
    private val repository: ProductRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var idProduct: Int = -1
    var product: Product? = null

    init {
        idProduct = savedStateHandle.get<Int>("idProduct")?.toInt()!!
        loadProductInfo()
    }

    fun loadProductInfo() {
        isLoading.value = true
        loadError.value = ""
        viewModelScope.launch {
            val result = repository.getProductInfo(idProduct)
            when(result) {
                is Resource.Success -> {
                    loadError.value = ""
                    isLoading.value = false
                    product = result.data
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                else -> {}
            }
        }
    }

}