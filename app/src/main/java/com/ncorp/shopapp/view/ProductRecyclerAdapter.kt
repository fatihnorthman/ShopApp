package com.ncorp.shopapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ncorp.shopapp.databinding.RecyclerRowBinding
import com.ncorp.shopapp.model.Product
import com.ncorp.shopapp.viewmodel.ProductViewModel

class ProductRecyclerAdapter(
    val productList: List<Product>,
    private val listener: Listener,
    private val viewModel: ProductViewModel? = null
) : RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder, position: Int
    ) {
        val product = productList[position]
        holder.binding.textViewProductName.text = product.name
        holder.binding.textViewProductPrice.text = "₺ ${product.price}"
        Glide.with(holder.itemView.context)
            .load(product.url)
            .placeholder(com.ncorp.shopapp.R.drawable.product_placeholder)
            .circleCrop()
            .into(holder.binding.imageViewProduct)
        holder.binding.buttonAddToCart.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Added to cart: ${product.name}",
                Toast.LENGTH_LONG
            ).show()
            listener.onItemClick(product)
        }
        // Favori ikonunu ayarla
        holder.binding.buttonFavorite.setImageResource(
            if (product.isFavorite) com.ncorp.shopapp.R.drawable.ic_favorite_filled
            else com.ncorp.shopapp.R.drawable.ic_favorite_border
        )
        holder.binding.buttonFavorite.setOnClickListener {
            viewModel?.toggleFavorite(product)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    interface Listener {
        fun onItemClick(product: Product)
    }

    class ViewHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {}
}