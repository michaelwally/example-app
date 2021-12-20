package com.breathwork.challenge.data

sealed class Discount {
    data class BulkDiscount(
        val productCode: String,
        val minimumAmount: Int,
        val discount: Float
    ) : Discount()

    // Discounts could be further generalized, ex: XForY discount
    data class TwoForOne(
        val productCode: String,
        val discount: Float
    ) : Discount()
}