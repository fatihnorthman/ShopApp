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
					response.body()?.let {
						productList.value = it
					}
				}
			}
		}
	}

	// Sepete ürün ekleme fonksiyonu.
	fun addtoBasket(product: Product) {
		if (basket.value != null) {
			// Eğer sepet doluysa, listeyi arrayListe çeviriyoruz.
			val arrayList = basket.value as ArrayList<Product>
			// Ürün zaten sepetteyse adetini artırıyoruz.
			if (arrayList.contains(product)) {
				val indexOfFirst = arrayList.indexOfFirst { it == product }
				val relatedProduct = arrayList.get(indexOfFirst)
				relatedProduct.count += 1
				basket.value = arrayList
			} else {
				// Ürün sepette yoksa adetini 1 yap ve listeye ekle.
				product.count += 1
				arrayList.add(product)
				basket.value = arrayList
			}
		} else {
			// Sepet boşsa yeni bir liste oluştur ve ürünü ekle.
			val arrayList = ArrayList<Product>()
			product.count += 1
			basket.value = arrayList
		}
		// Sepet güncellendikten sonra toplam fiyat hesaplanır.
		refreshTotalValue(basket.value!!)
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

	// ViewModel temizlenirken aktif coroutine işi iptal edilir.
	override fun onCleared() {
		super.onCleared()
		job?.cancel()
	}
}
