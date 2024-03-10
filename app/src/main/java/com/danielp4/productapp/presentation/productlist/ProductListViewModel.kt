package com.danielp4.productapp.presentation.productlist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielp4.productapp.data.models.ProductListEntry
import com.danielp4.productapp.repository.ProductRepository
import com.danielp4.productapp.util.Constants.PAGE_SIZE
import com.danielp4.productapp.util.Resource
import com.danielp4.productapp.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private var curPage = 0

    var productList = mutableStateOf<List<ProductListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedProductList = listOf<ProductListEntry>()

    var searchText = mutableStateOf("")
        private set
    var isSearching = mutableStateOf(false)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadProductPaginated()
    }

    fun onEvent(event: ProductListEvent) {
        when(event) {
            ProductListEvent.OnClickDeleteSearchText -> {
                productList.value = cachedProductList
                searchText.value = ""
                isSearching.value = false
            }
            is ProductListEvent.OnProductClick -> {
                sendUiEvent(UiEvent.Navigate(event.route))
                searchText.value = ""
                isSearching.value = false
            }
            is ProductListEvent.OnTextSearchChange -> {
                isSearching.value = true
                searchText.value = event.text
                productList.value = cachedProductList.filter { product ->
                    product.title.lowercase().startsWith(searchText.value.lowercase())
                }
            }
        }
    }

    fun loadProductPaginated() {
        isLoading.value = true
        loadError.value = ""
        viewModelScope.launch {
            val result = repository.getProductList(curPage * PAGE_SIZE, PAGE_SIZE)
            when(result) {
                is Resource.Success -> {
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.total
                    val productEntries = result.data.products.mapIndexed { index, entry ->
                        ProductListEntry(entry.id, entry.title, entry.description, entry.thumbnail, entry.price, entry.brand)
                    }
                    curPage++
                    loadError.value = ""
                    isLoading.value = false
                    productList.value += productEntries
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                else -> {}
            }
        }
        cachedProductList = productList.value
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}