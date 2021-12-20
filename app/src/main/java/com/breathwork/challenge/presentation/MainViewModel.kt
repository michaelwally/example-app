package com.breathwork.challenge.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breathwork.challenge.data.Discount
import com.breathwork.challenge.data.Product
import com.breathwork.challenge.domain.CalculateReceiptUseCase
import com.breathwork.challenge.domain.GetDiscountsUseCase
import com.breathwork.challenge.domain.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val calculateReceiptUseCase: CalculateReceiptUseCase,
    getDiscountsUseCase: GetDiscountsUseCase,
) : ViewModel() {

    // Using this pattern of private Mutable and public Immutable so not to expose mutability
    private val _productsStateData: MutableLiveData<State<List<Product>>> = MutableLiveData()
    val productsStateData: LiveData<State<List<Product>>>
        get() = _productsStateData
    private val _total: MutableLiveData<Float> = MutableLiveData(0f)
    val total: LiveData<Float>
        get() = _total
    private val _purchasedProducts: MutableMap<Product, Int> = mutableMapOf()

    private var discountsApplied: Map<Discount, Int> = mutableMapOf()
    private val availableDiscounts = getDiscountsUseCase()

    fun refreshProducts() {
        viewModelScope.launch {
            getProducts().collect { _productsStateData.postValue(it) }
        }
    }

    fun purchaseProduct(product: Product) {
        var count = 1
        _purchasedProducts[product]?.let {
            count = it + 1
        }
        _purchasedProducts[product] = count
        calculateReceipt()
    }

    fun removeProduct(product: Product) {
        var count = 0
        _purchasedProducts[product]?.let {
            count = max(it - 1, 0)
        }
        _purchasedProducts[product] = count
        calculateReceipt()
    }

    fun hasDiscount(product: Product) = availableDiscounts[product.code] != null

    fun hasDiscountApplied(product: Product) = availableDiscounts[product.code]?.let { discount ->
        val discountAppliedCount = discountsApplied[discount]
        return discountAppliedCount != null && discountAppliedCount > 0
    } ?: false

    fun purchaseCount(product: Product): Int = _purchasedProducts[product] ?: 0

    // The receipt could more optimally be kept as a running talley but that might require some
    // additional state / maintenance
    private fun calculateReceipt() {
        val receipt = calculateReceiptUseCase(availableDiscounts, _purchasedProducts)
        _total.postValue(receipt.total)
        discountsApplied = receipt.discountsApplied
    }

    private fun getProducts() = flow {
        emit(State.LoadingState)
        try {
            emit(State.SuccessState(getProductsUseCase()))
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(exception.resolveFailure())
        }
    }
}