package com.ncorp.shopapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ncorp.shopapp.databinding.ItemBasketRowBinding
import com.ncorp.shopapp.model.Product

// RecyclerView Adapter'ı: Sepet ürünlerini gösterir
class BasketRecyclerAdapter(
	val basketList: ArrayList<Product>, // Gösterilecek ürün listesi
	private val onItemAction: (Product, ActionType) -> Unit // Ürün işlem callback'i
) : RecyclerView.Adapter<BasketRecyclerAdapter.ViewHolder>() {

	// Yapılacak işlemler enum’u: Şimdilik sadece adet azaltma var
	enum class ActionType { DECREASE }

	// ViewHolder oluşturur ve ItemBasketRowBinding ile bağlar
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val binding =
			ItemBasketRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ViewHolder(binding)
	}

	// ViewHolder’a ürün verilerini bağlar
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val product = basketList[position]

		// Ürün adı, fiyatı, adet bilgisi TextView’lara set edilir
		holder.binding.textViewBasketProductName.text = product.name
		holder.binding.textViewBasketPrice.text = "₺ ${product.price}"
		holder.binding.textViewQuantity.text = "Adet: ${product.count}"

		// Glide ile ürün resmi yüklenir
		Glide.with(holder.itemView.context).load(product.url)
			.into(holder.binding.imageViewBasketProduct)

		// Silme/azaltma butonuna basıldığında callback çağrılır
		holder.binding.buttonRemoveItem.setOnClickListener {
			onItemAction(product, BasketRecyclerAdapter.ActionType.DECREASE)
		}
	}

	// Toplam ürün sayısı
	override fun getItemCount(): Int {
		return basketList.size
	}

	// ViewHolder sınıfı: binding ile view’ları tutar
	class ViewHolder(val binding: ItemBasketRowBinding) : RecyclerView.ViewHolder(binding.root)
}
