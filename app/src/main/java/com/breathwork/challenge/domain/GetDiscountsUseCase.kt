package com.breathwork.challenge.domain

import com.breathwork.challenge.data.Discount
import javax.inject.Inject

class GetDiscountsUseCase @Inject constructor() {

    operator fun invoke(): Map<String, Discount> {
        // Discounts could come from API, database, locl file etc. Created them here in interest of time
        val bulkDiscount = Discount.BulkDiscount("TSHIRT", 3, 1f)
        val twoForOneDiscount = Discount.TwoForOne("PEN", 5f)
        val codeDiscountMap = mutableMapOf<String, Discount>()
        codeDiscountMap[bulkDiscount.productCode] = bulkDiscount
        codeDiscountMap[twoForOneDiscount.productCode] = twoForOneDiscount
        return codeDiscountMap
    }
}