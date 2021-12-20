package com.breathwork.challenge.domain

import com.breathwork.challenge.data.Discount
import com.breathwork.challenge.data.Product
import com.breathwork.challenge.data.Receipt
import javax.inject.Inject

class CalculateReceiptUseCase @Inject constructor() {
    operator fun invoke(
        availableDiscounts: Map<String, Discount>,
        purchases: Map<Product, Int>
    ): Receipt {
        val runningTotal = purchases.getTotal()
        val discountsApplied = purchases.getDiscountsApplied(availableDiscounts)
        val total = discountsApplied.calculateTotal(runningTotal)
        return Receipt(discountsApplied, total)
    }
}

private fun Product.calculateCost(count: Int) = count * price

private fun Map<Product, Int>.getTotal() =
    keys.fold(0f) { acc, product ->
        val count = this[product] ?: 0
        acc + product.calculateCost(count)
    }

private fun Map<Product, Int>.getDiscountsApplied(availableDiscounts: Map<String, Discount>):
        Map<Discount, Int> {
    val discountsApplied = mutableMapOf<Discount, Int>()
    keys.forEach { product ->
        val discount = availableDiscounts[product.code]
        discount?.let {
            val purchaseCount = this[product] ?: 0
            val appliedCount = discount.calculateAppliesCount(purchaseCount)
            if (appliedCount > 0) {
                discountsApplied[discount] = appliedCount
            }
        }
    }
    return discountsApplied
}

private fun Map<Discount, Int>.calculateTotal(runningTotal: Float): Float {
    val totalDiscount = calculateTotalDiscount()
    return runningTotal - totalDiscount
}

private fun Map<Discount, Int>.calculateTotalDiscount() =
    keys.fold(0f) { acc, discount ->
        val timesApplied = this[discount] ?: 0
        acc + (discount.getDiscount() * timesApplied)
    }

private fun Discount.calculateAppliesCount(purchaseCount: Int) =
    when (this) {
        is Discount.BulkDiscount ->
            if (purchaseCount >= this.minimumAmount) {
                purchaseCount
            } else {
                0
            }
        is Discount.TwoForOne ->
            if (purchaseCount > 1) {
                purchaseCount / 2
            } else {
                0
            }
    }

private fun Discount.getDiscount() = when (this) {
    is Discount.TwoForOne -> this.discount
    is Discount.BulkDiscount -> this.discount
}