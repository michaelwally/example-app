package com.breathwork.challenge.data

import javax.inject.Inject

// Repository might normally serve to return data from both database and API
class ProductsRepository @Inject constructor(
    private val productsApi: ProductsApi
) {
    suspend fun getProducts(): List<Product> = productsApi.getProducts()
}