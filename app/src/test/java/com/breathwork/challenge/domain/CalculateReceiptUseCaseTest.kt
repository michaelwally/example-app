package com.breathwork.challenge.domain

import com.breathwork.challenge.data.Product
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateReceiptUseCaseTest {

    @Test
    fun calculateReceipt_case_noPurchases() {
        val calculateReceiptUseCase = CalculateReceiptUseCase()
        val receipt = calculateReceiptUseCase(getDiscounts(), mapOf())
        assertEquals(receipt.total, 0f)
        receipt.discountsApplied.forEach { (_, count) ->
            assertEquals(count, 0)
        }
    }

    @Test
    fun calculateReceipt_case_examples() {
        val calculateReceiptUseCase = CalculateReceiptUseCase()
        val receipt1 = calculateReceiptUseCase(getDiscounts(), testMap1)
        assertEquals(receipt1.total, 32.50f)
        val receipt2 = calculateReceiptUseCase(getDiscounts(), testMap2)
        assertEquals(receipt2.total, 25f)
        val receipt3 = calculateReceiptUseCase(getDiscounts(), testMap3)
        assertEquals(receipt3.total, 81f)
        val receipt4 = calculateReceiptUseCase(getDiscounts(), testMap4)
        assertEquals(receipt4.total, 74.50f)
    }

    private fun getDiscounts() = GetDiscountsUseCase()()

    private val pen = Product("PEN", "Pen", 5f)

    private val hat = Product("HAT", "Nice Hat", 12.25f)

    private val tShirt = Product("TSHIRT", "T-Shirt", 20.00f)

    private val mug = Product("MUG", "Coffee Mug", 7.50f)

    private val testMap1 = mapOf(Pair(pen, 1), Pair(tShirt, 1), Pair(mug, 1))

    private val testMap2 = mapOf(Pair(pen, 2), Pair(tShirt, 1))

    private val testMap3 = mapOf(Pair(pen, 1), Pair(tShirt, 4))

    private val testMap4 = mapOf(Pair(pen, 3), Pair(tShirt, 3), Pair(mug, 1))

}