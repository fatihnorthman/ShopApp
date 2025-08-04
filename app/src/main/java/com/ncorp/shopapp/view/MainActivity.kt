package com.ncorp.shopapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavController
import com.ncorp.shopapp.R
import com.ncorp.shopapp.databinding.ActivityMainBinding
import android.view.animation.AnimationUtils

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navStore: LinearLayout
    private lateinit var navCategories: LinearLayout
    private lateinit var navBasket: LinearLayout
    private lateinit var navProfile: LinearLayout
    
    private lateinit var iconStore: ImageView
    private lateinit var iconCategories: ImageView
    private lateinit var iconBasket: ImageView
    private lateinit var iconProfile: ImageView
    
    private lateinit var textStore: TextView
    private lateinit var textCategories: TextView
    private lateinit var textBasket: TextView
    private lateinit var textProfile: TextView
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) // Koyu tema
        enableEdgeToEdge()
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // WindowInsets ile alt padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            // customBottomBar'a padding eklenmeyecek
            insets
        }

        // Navigation controller
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        // Custom bottom bar view'larını bul
        navStore = findViewById(R.id.navStore)
        navCategories = findViewById(R.id.navCategories)
        navBasket = findViewById(R.id.navBasket)
        navProfile = findViewById(R.id.navProfile)
        
        iconStore = findViewById(R.id.iconStore)
        iconCategories = findViewById(R.id.iconCategories)
        iconBasket = findViewById(R.id.iconBasket)
        iconProfile = findViewById(R.id.iconProfile)
        
        textStore = findViewById(R.id.textStore)
        textCategories = findViewById(R.id.textCategories)
        textBasket = findViewById(R.id.textBasket)
        textProfile = findViewById(R.id.textProfile)
        

        // Başlangıçta seçili olanı ayarla
        setSelectedTab(R.id.storeFragment)

        // Tıklama ile fragment geçişi ve animasyon
        navStore.setOnClickListener {
            if (navController.currentDestination?.id != R.id.storeFragment) {
                when (navController.currentDestination?.id) {
                    R.id.categoriesFragment -> navController.navigate(R.id.action_categories_to_store)
                    R.id.basketFragment -> navController.navigate(R.id.action_basket_to_store)
                    R.id.profileFragment -> navController.navigate(R.id.action_profile_to_store)
                    else -> navController.navigate(R.id.storeFragment)
                }
                setSelectedTab(R.id.storeFragment)
            }
        }
        
        navCategories.setOnClickListener {
            if (navController.currentDestination?.id != R.id.categoriesFragment) {
                when (navController.currentDestination?.id) {
                    R.id.storeFragment -> navController.navigate(R.id.action_store_to_categories)
                    R.id.basketFragment -> navController.navigate(R.id.action_basket_to_categories)
                    R.id.profileFragment -> navController.navigate(R.id.action_profile_to_categories)
                    else -> navController.navigate(R.id.categoriesFragment)
                }
                setSelectedTab(R.id.categoriesFragment)
            }
        }
        
        navBasket.setOnClickListener {
            if (navController.currentDestination?.id != R.id.basketFragment) {
                when (navController.currentDestination?.id) {
                    R.id.storeFragment -> navController.navigate(R.id.action_store_to_basket)
                    R.id.categoriesFragment -> navController.navigate(R.id.action_categories_to_basket)
                    R.id.profileFragment -> navController.navigate(R.id.action_profile_to_basket)
                    else -> navController.navigate(R.id.basketFragment)
                }
                setSelectedTab(R.id.basketFragment)
            }
        }
        
        navProfile.setOnClickListener {
            if (navController.currentDestination?.id != R.id.profileFragment) {
                when (navController.currentDestination?.id) {
                    R.id.storeFragment -> navController.navigate(R.id.action_store_to_profile)
                    R.id.categoriesFragment -> navController.navigate(R.id.action_categories_to_profile)
                    R.id.basketFragment -> navController.navigate(R.id.action_basket_to_profile)
                    else -> navController.navigate(R.id.profileFragment)
                }
                setSelectedTab(R.id.profileFragment)
            }
        }

        // Navigation değişiminde seçili tab'ı güncelle
        navController.addOnDestinationChangedListener { _, destination, _ ->
            setSelectedTab(destination.id)
        }
    }

    private fun setSelectedTab(selectedId: Int) {
        val activeColor = ContextCompat.getColor(this, R.color.primary)
        val inactiveColor = ContextCompat.getColor(this, R.color.text_secondary)
        val scaleAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_nav_scale)

        // Mağaza
        if (selectedId == R.id.storeFragment) {
            iconStore.setColorFilter(activeColor)
            textStore.setTextColor(activeColor)
            iconStore.startAnimation(scaleAnim)
            textStore.startAnimation(scaleAnim)
        } else {
            iconStore.setColorFilter(inactiveColor)
            textStore.setTextColor(inactiveColor)
        }
        // Kategoriler
        if (selectedId == R.id.categoriesFragment) {
            iconCategories.setColorFilter(activeColor)
            textCategories.setTextColor(activeColor)
            iconCategories.startAnimation(scaleAnim)
            textCategories.startAnimation(scaleAnim)
        } else {
            iconCategories.setColorFilter(inactiveColor)
            textCategories.setTextColor(inactiveColor)
        }
        // Sepet
        if (selectedId == R.id.basketFragment) {
            iconBasket.setColorFilter(activeColor)
            textBasket.setTextColor(activeColor)
            iconBasket.startAnimation(scaleAnim)
            textBasket.startAnimation(scaleAnim)
        } else {
            iconBasket.setColorFilter(inactiveColor)
            textBasket.setTextColor(inactiveColor)
        }
        // Profil
        if (selectedId == R.id.profileFragment) {
            iconProfile.setColorFilter(activeColor)
            textProfile.setTextColor(activeColor)
            iconProfile.startAnimation(scaleAnim)
            textProfile.startAnimation(scaleAnim)
        } else {
            iconProfile.setColorFilter(inactiveColor)
            textProfile.setTextColor(inactiveColor)
        }
    }

    private fun animateTab(view: View) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1.15f, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1.15f, 1.0f)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY)
        animatorSet.duration = 250
        animatorSet.start()
    }
}
