package com.breathwork.challenge.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breathwork.challenge.R
import com.breathwork.challenge.data.Product
import com.breathwork.challenge.databinding.ActivityMainBinding
import com.breathwork.challenge.databinding.ItemProductBinding
import java.text.NumberFormat

class MainView(
    private val binding: ActivityMainBinding,
    viewModel: MainViewModel,
    lifecycleOwner: LifecycleOwner
) {
    private val adapter = ProductAdapter(viewModel)

    init {
        viewModel.refreshProducts() // Might also refresh if implemented pull to refresh
        with(binding) {
            recycler.layoutManager = LinearLayoutManager(root.context)
            recycler.adapter = adapter

            viewModel.productsStateData.observe(lifecycleOwner) {
                onProductStateDataChanged(it)
            }
            viewModel.total.observe(lifecycleOwner) {
                totalText.text = root.context.getString(R.string.total_format, it.formatCurrency())
            }
        }
    }

    private fun onProductStateDataChanged(state: State<List<Product>>) {
        binding.loadingIndicator.isVisible = state is State.LoadingState
        binding.errorText.isVisible = state is State.FailureState
        when (state) {
            is State.SuccessState -> adapter.updateProducts(state.result)
            is State.FailureState -> binding.errorText.text = state.message
        }
    }
}

private class ProductAdapter(
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<ProductViewHolder>() {

    private val products: MutableList<Product> = mutableListOf()

    fun updateProducts(products: List<Product>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged() // Preferred to use DiffUtil, used notify in interest of time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position], mainViewModel)
    }

    override fun getItemCount(): Int {
        return products.size
    }
}

private class ProductViewHolder(private val binding: ItemProductBinding) :
    RecyclerView.ViewHolder(binding.root) {

    // Alternately the app could maintain a ViewModel per product to hold the transient state
    // Used a single MainViewModel as it eas a bit easier, faster
    fun bind(product: Product, mainViewModel: MainViewModel) {
        with(binding) {
            nameText.text = product.name
            priceText.text = product.price.formatCurrency()
            update(product, mainViewModel)

            plusButton.setOnClickListener {
                mainViewModel.purchaseProduct(product)
                update(product, mainViewModel)
            }
            minusButton.setOnClickListener {
                mainViewModel.removeProduct(product)
                update(product, mainViewModel)
            }
        }
    }

    // Rather than updating as a response to known events, it would be cleaner to listen to live data
    // This was done since I used a single view model to hold all the product state data, as
    // convenience.
    private fun update(product: Product, mainViewModel: MainViewModel) {
        with(binding) {
            discountText.text = getDiscountText(
                mainViewModel.hasDiscount(product),
                mainViewModel.hasDiscountApplied(product)
            )
            countText.text = "${mainViewModel.purchaseCount(product)}"
        }
    }

    private fun getDiscountText(hasDiscount: Boolean, hasDiscountApplied: Boolean): String? {
        return when {
            hasDiscountApplied -> binding.root.context.getString(R.string.has_discount_applied)
            hasDiscount -> binding.root.context.getString(R.string.has_discount)
            else -> null
        }
    }
}

private fun Float.formatCurrency(): String {
    val format = NumberFormat.getCurrencyInstance()
    return format.format(this)
}