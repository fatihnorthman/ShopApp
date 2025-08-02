plugins {
	id("com.android.application")
	alias(libs.plugins.kotlin.android)
	kotlin("kapt")

}

android {
	namespace = "com.ncorp.shopapp"
	compileSdk = 36

	defaultConfig {
		applicationId = "com.ncorp.shopapp"
		minSdk = 27
		targetSdk = 36
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	buildFeatures {
		dataBinding = true
	}
	buildFeatures { viewBinding = true }
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
}


// Navigation component sürümü
val navVersion = "2.9.3"
// Retrofit sürümü
val retrofitVersion = "3.0.0"
// Lifecycle component sürümü
val lifecycleVersion = "2.9.2"

dependencies {
	// Kotlin standart kütüphanesi
	implementation("org.jetbrains.kotlin:kotlin-stdlib")
	// AndroidX Core KTX uzantıları
	implementation(libs.androidx.core.ktx)
	// AndroidX AppCompat kütüphanesi
	implementation(libs.androidx.appcompat)
	// Material Design componentleri
	implementation(libs.material)
	// ConstraintLayout kütüphanesi
	implementation(libs.androidx.constraintlayout)
	// Navigation Component - Fragment KTX
	implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
	// Navigation Component - UI KTX
	implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
	// Eski Android sürümleri için destek kütüphanesi
	implementation("androidx.legacy:legacy-support-v4:1.0.0")
	implementation(libs.androidx.fragment)
	implementation(libs.androidx.recyclerview)

	// Test bağımlılıkları
	testImplementation("junit:junit:4.+")
	androidTestImplementation("androidx.test.ext:junit:1.3.0")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

	// Glide - Resim yükleme ve önbellekleme kütüphanesi
	implementation("com.github.bumptech.glide:glide:4.16.0")
	kapt("com.github.bumptech.glide:compiler:4.16.0")

	// Retrofit - HTTP istemcisi
	implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
	implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion") // Gson dönüştürücü
	// Kotlin Coroutines - Asenkron programlama için
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

	// ViewModel ve LiveData - Lifecycle Aware componentler
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
	implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
	// Fragment KTX uzantıları
	implementation("androidx.fragment:fragment-ktx:1.8.8")
	// Activity KTX uzantıları
	implementation(libs.androidx.activity)
}