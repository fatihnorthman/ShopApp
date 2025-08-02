package com.ncorp.shopapp.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ncorp.shopapp.R

// Uygulamanın ana aktivitesi
class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// Gece modunu kapat (sadece açık tema)
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

		// Kenardan kenara UI modu açılır
		enableEdgeToEdge()

		// Layout ayarlanır
		setContentView(R.layout.activity_main)

		// Sistem barlarının görünümüne göre view padding ayarı yapılır
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
			insets
		}

		// Navigation controller alınır
		val navController =
			(supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController

		// BottomNavigationView ile navigation controller bağlanır
		findViewById<BottomNavigationView>(R.id.bottomNavView).setupWithNavController(navController)
	}
}
