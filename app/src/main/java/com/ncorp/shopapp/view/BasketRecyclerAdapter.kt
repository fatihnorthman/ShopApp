package com.ncorp.shopapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ncorp.shopapp.databinding.ItemBasketRowBinding
import com.ncorp.shopapp.model.Product

class BasketRecyclerAdapter(
	val basketList: ArrayList<Product>,
	private val onItemAction: (Product, ActionType) -> Unit
) :
	RecyclerView.Adapter<BasketRecyclerAdapter.ViewHolder>() {
	enum class ActionType { DELETE, DECREASE }
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): ViewHolder {
		val binding =
			ItemBasketRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ViewHolder(binding)

	}

	override fun onBindViewHolder(
		holder: ViewHolder,
		position: Int
	) {
		val product = basketList[position]
		holder.binding.textViewBasketProductName.text = basketList[position].name
		holder.binding.textViewBasketPrice.text = "₺ ${basketList.get(position).price.toString()}"
		holder.binding.textViewQuantity.text = "Adet: ${basketList[position].count}"
		Glide.with(holder.itemView.context).load(basketList[position].url)
			.into(holder.binding.imageViewBasketProduct)

		holder.binding.buttonRemoveItem.setOnClickListener {
			onItemAction(product, ActionType.DELETE)
		}

		holder.binding.root.setOnClickListener {
			onItemAction(product, ActionType.DECREASE)
		}

	}



	override fun getItemCount(): Int {
		return basketList.size
	}

	class ViewHolder(val binding: ItemBasketRowBinding) : RecyclerView.ViewHolder(binding.root) {}
}