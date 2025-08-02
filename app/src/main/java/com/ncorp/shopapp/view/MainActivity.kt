// Paket bildirimi: Bu dosyanın ait olduğu paketi belirtir.
package com.ncorp.shopapp.view

// Gerekli importlar: Kullanılan sınıfların ve fonksiyonların dahil edilmesi.
import android.os.Bundle
import androidx.activity.enableEdgeToEdge // Kenardan kenara görünümü etkinleştirmek için.
import androidx.appcompat.app.AppCompatActivity // Temel aktivite sınıfı.
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat // View uyumluluk yardımcıları.
import androidx.core.view.WindowInsetsCompat // Pencere iç boşlukları için uyumluluk.
import androidx.navigation.fragment.NavHostFragment // Navigasyon için fragment barındırıcısı.
import androidx.navigation.ui.setupWithNavController // Navigasyon kontrolcüsünü UI bileşenleriyle ayarlamak için.
import com.google.android.material.bottomnavigation.BottomNavigationView // Alt navigasyon çubuğu bileşeni.
import com.ncorp.shopapp.R

// MainActivity sınıfı, uygulamanın ana aktivitesini temsil eder.
class MainActivity : AppCompatActivity() {
	// onCreate metodu, aktivite oluşturulduğunda çağrılır.
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState) // Üst sınıfın onCreate metodunu çağırır.
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
		enableEdgeToEdge() // Kenardan kenara görünümü etkinleştirir.
		setContentView(R.layout.activity_main) // Aktivitenin layout'unu ayarlar.

		// Pencere iç boşluklarını (insets) dinleyici ekler.
		// Bu, sistem çubukları (durum çubuğu, navigasyon çubuğu) için uygun dolguyu ayarlamaya yardımcı olur.
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()) // Sistem çubuklarının boyutlarını alır.
			// View'in dolgusunu sistem çubuklarına göre ayarlar (alt dolgu hariç).
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
			insets // Değiştirilmiş veya orijinal insets'i döndürür.
		}

		// NavHostFragment'ı bulur ve NavController'ı alır.
		val navController = (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController
		// BottomNavigationView'ı bulur ve NavController ile ayarlar.
		findViewById<BottomNavigationView>(R.id.bottomNavView).setupWithNavController(navController)
	}
}