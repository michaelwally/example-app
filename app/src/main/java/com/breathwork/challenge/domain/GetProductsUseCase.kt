package com.breathwork.challenge.domain

import com.breathwork.challenge.data.Product
import com.breathwork.challenge.data.ProductsRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(): List<Product> = productsRepository.getProducts()
}