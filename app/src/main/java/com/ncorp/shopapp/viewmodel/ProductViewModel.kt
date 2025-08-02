package com.ncorp.shopapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncorp.shopapp.model.Product
import com.ncorp.shopapp.services.ProductAPI

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductViewModel : ViewModel() {
	private var job: Job? = null
	val productList = MutableLiveData<List<Product>>()
	val basket = MutableLiveData<List<Product>>()
	val totalBasket = MutableLiveData<Int>()


	fun downloadData() {
		val retrofit = Retrofit.Builder()
			.baseUrl("https://raw.githubusercontent.com/")
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(ProductAPI::class.java)
		job = viewModelScope.launch(Dispatchers.IO) {
			val response = retrofit.getData()
			withContext(Dispatchers.Main) {
				if (response.isSuccessful) {
					response.body()?.let {
						productList.value = it

					}
				}
			}
		}
	}

	fun addtoBasket(product: Product) {
		if (basket.value != null) {

			val arrayList = basket.value as ArrayList<Product>
			if (arrayList.contains(product)) {
				val indexOfFirst = arrayList.indexOfFirst { it == product }
				val relatedProduct = arrayList.get(indexOfFirst)
				relatedProduct.count += 1
				basket.value = arrayList
			} else {
				product.count += 1
				arrayList.add(product)
				basket.value = arrayList
			}


		} else {
			val arrayList = ArrayList<Product>()
			product.count += 1
			basket.value = arrayList
		}
		refreshTotalValue(basket.value!!)


	}

	private fun refreshTotalValue(list: List<Product>) {
		var total = 0
		list.forEach {
			val price = it.price.toIntOrNull() ?: 0
			total += price * it.count
		}
		totalBasket.value = total
	}

	fun deleteProductFromBasket(product: Product) {
		if (basket.value != null) {
			val arrayList = basket.value as ArrayList
			arrayList.remove(product)
			basket.value = arrayList
			refreshTotalValue(arrayList)
		}
	}
	fun decreaseProductCount(product: Product) {
		val current = basket.value as? ArrayList<Product> ?: return
		val index = current.indexOfFirst { it.id == product.id }
		if (index != -1) {
			val item = current[index]
			if (item.count > 1) {
				item.count--
			} else {
				current.removeAt(index)
			}
			basket.value = current
			refreshTotalValue(current)
		}
	}


	override fun onCleared() {
		super.onCleared()
		job?.cancel()
	}
}