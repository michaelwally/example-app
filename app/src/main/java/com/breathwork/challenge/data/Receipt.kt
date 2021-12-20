package com.breathwork.challenge.data

data class Receipt(
    val discountsApplied: Map<Discount, Int>,
    val total: Float
)