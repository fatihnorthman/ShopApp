// Paket bildirimi: Bu sınıfın ait olduğu paket.
package com.ncorp.shopapp.viewmodel

// Gerekli importlar
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
import com.ncorp.shopapp.model.Order

// ViewModel sınıfı: UI'dan bağımsız, veri ve iş mantığını tutar.
// Bu sınıf ürün listesini indirir, sepete ekler, çıkarır ve toplam tutarı hesaplar.
class ProductViewModel : ViewModel() {
	// Coroutine işi, veri indirme işlemi iptal edilebilir olması için saklanıyor.
	private var job: Job? = null

	// Canlı veri: ürünlerin listesi
	val productList = MutableLiveData<List<Product>>()

	// Canlı veri: sepetteki ürünlerin listesi
	val basket = MutableLiveData<List<Product>>()

	// Canlı veri: sepetin toplam tutarı
	val totalBasket = MutableLiveData<Int>()

	// Favori ürünlerin listesi
	private val favoriteIds = mutableSetOf<String>()
	val favoriteProducts = MutableLiveData<List<Product>>()

	// Siparişlerin listesi
	val orders = MutableLiveData<List<Order>>(emptyList())

	// Ürün listesini internetten indirip 'productList' LiveData'sına koyar.
	fun downloadData() {
		// Retrofit yapılandırması: Temel URL ve JSON dönüştürücü ayarlanır.
		val retrofit = Retrofit.Builder()
			.baseUrl("https://raw.githubusercontent.com/")
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(ProductAPI::class.java)

		// Coroutine başlatılır, IO dispatcher ile arka planda ağ isteği yapılır.
		job = viewModelScope.launch(Dispatchers.IO) {
			val response = retrofit.getData()
			withContext(Dispatchers.Main) {
				// Eğer istek başarılıysa,
				if (response.isSuccessful) {
					// Gelen ürün listesi LiveData'ya atanır,
					response.body()?.let { list ->
						// Favori id'leri koruyarak isFavorite alanını set et
						val updated = list.map { it.copy(isFavorite = favoriteIds.contains(it.id)) }
						productList.value = updated
						favoriteProducts.value = updated.filter { it.isFavorite }
					}
				}
			}
		}
	}

	// Sepete ürün ekleme fonksiyonu.
	fun addtoBasket(product: Product) {
		val currentList = basket.value?.toMutableList() ?: mutableListOf()
		val index = currentList.indexOfFirst { it.id == product.id }
		if (index != -1) {
			val existing = currentList[index]
			currentList[index] = existing.copy(count = existing.count + 1)
		} else {
			currentList.add(product.copy(count = 1))
		}
		basket.value = currentList
		refreshTotalValue(currentList)
	}

	// Sepetteki ürünlerin toplam fiyatını hesaplayan özel fonksiyon.
	private fun refreshTotalValue(listOfProduct: List<Product>) {
		var total = 0
		// Listedeki her ürünün fiyatı ve adetini çarparak toplam tutara ekler.
		listOfProduct.forEach { product ->
			val price = product.price.toIntOrNull()
			price?.let {
				val count = product.count
				val revenue = count * it
				total += revenue
			}
		}
		// Hesaplanan toplam değer LiveData'ya atanır.
		totalBasket.value = total
	}

	// Sepetten tamamen ürün silme fonksiyonu.
	fun deleteProductFromBasket(product: Product) {
		if (basket.value != null) {
			val arrayList = basket.value as ArrayList<Product>
			// Ürün tamamen listeden çıkarılır.
			arrayList.remove(product)
			basket.value = arrayList
			refreshTotalValue(arrayList)
		}
	}

	// Sepetteki ürün adedini 1 azaltan veya adeti 1 ise ürünü tamamen çıkaran fonksiyon.
	fun decreaseProductCount(product: Product) {
		// Sepet listesini alır, yoksa fonksiyondan çıkar.
		val current = basket.value as? ArrayList<Product> ?: return
		// Ürünün listedeki indeksini bulur.
		val index = current.indexOfFirst { it.id == product.id }
		if (index != -1) {
			val item = current[index]
			if (item.count > 1) {
				// Adet 1'den fazla ise azalt
				item.count--
			} else {
				// Adet 1 ise ürünü tamamen çıkar
				current.removeAt(index)
			}
			// Güncellenen listeyi LiveData'ya ata ve toplamı yenile
			basket.value = current
			refreshTotalValue(current)
		}
	}

	// Favori ürünleri güncelleyen fonksiyon.
	fun toggleFavorite(product: Product) {
		if (favoriteIds.contains(product.id)) {
			favoriteIds.remove(product.id)
		} else {
			favoriteIds.add(product.id)
		}
		updateFavoritesInProductList()
	}

	// Siparişi tamamla fonksiyonu.
	fun completeOrder() {
		val basketList = basket.value ?: emptyList()
		if (basketList.isNotEmpty()) {
			val total = totalBasket.value ?: 0
			val newOrder = Order(basketList.map { it.copy() }, total)
			val updatedOrders = orders.value?.toMutableList() ?: mutableListOf()
			updatedOrders.add(0, newOrder)
			orders.value = updatedOrders
			basket.value = emptyList()
			totalBasket.value = 0
		}
	}

	private fun updateFavoritesInProductList() {
		val updatedList = productList.value?.map {
			it.copy(isFavorite = favoriteIds.contains(it.id))
		} ?: emptyList()
		productList.value = updatedList
		favoriteProducts.value = updatedList.filter { it.isFavorite }
	}

	// ViewModel temizlenirken aktif coroutine işi iptal edilir.
	override fun onCleared() {
		super.onCleared()
		job?.cancel()
	}
}
