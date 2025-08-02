package com.ncorp.shopapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ncorp.shopapp.databinding.RecyclerRowBinding
import com.ncorp.shopapp.model.Product

class ProductRecyclerAdapter(val productList: List<Product>, private val listener: Listener) :
	RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder>() {
	override fun onCreateViewHolder(
		parent: ViewGroup, viewType: Int
	): ViewHolder {
		val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ViewHolder(binding)
	}

	override fun onBindViewHolder(
		holder: ViewHolder, position: Int
	) {
		holder.binding.textViewProductName.text = productList.get(position).name
		holder.binding.textViewProductPrice.text = "₺ ${productList.get(position).price.toString()}"
		Glide.with(holder.itemView.context).load(productList.get(position).url)
			.into(holder.binding.imageViewProduct)
		holder.binding.buttonAddToCart.setOnClickListener {
			Toast.makeText(
				holder.itemView.context,
				"Added to cart: ${productList.get(position).name}",
				Toast.LENGTH_LONG
			).show()
			listener.onItemClick(productList.get(position))
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