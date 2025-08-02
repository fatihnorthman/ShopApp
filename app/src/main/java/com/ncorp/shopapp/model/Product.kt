// Paket bildirimi
package com.ncorp.shopapp.model

// Veri sınıfı: Ürünü temsil eder.
// 'data' keyword otomatik equals, toString, copy vb oluşturur.
data class Product(
	val id: String,       // Ürünün benzersiz ID'si
	val name: String,     // Ürün ismi
	val price: String,    // Ürün fiyatı (string olarak tutuluyor, dikkat!)
	val url: String       // Ürün resmi URL'si
) {
	// Sepette bu ürünün adedini tutan değişken.
	var count: Int = 0   // Varsayılan 0, sepete eklendikçe artar
}
