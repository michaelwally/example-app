package com.breathwork.challenge.data

import retrofit2.http.GET

interface ProductsApi {

    @GET("/products")
    suspend fun getProducts(): List<Product>
}