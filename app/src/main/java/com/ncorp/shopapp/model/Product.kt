// Paket bildirimi
package com.ncorp.shopapp.model

// Veri sınıfı: Ürünü temsil eder.
// 'data' keyword otomatik equals, toString, copy vb oluşturur.
data class Product(
	val id: String,       // Ürünün benzersiz ID'si
	val name: String,     // Ürün ismi
	val price: String,    // Ürün fiyatı (string olarak tutuluyor)
	val url: String,      // Ürün resmi URL'si
	var count: Int = 0,    // Sepetteki adedi (varsayılan 0)
	var isFavorite: Boolean = false
)
