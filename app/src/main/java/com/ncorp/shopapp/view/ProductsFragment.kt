package com.ncorp.shopapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ncorp.shopapp.R
import com.ncorp.shopapp.services.ProductAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProductsFragment : Fragment() {


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_products, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		downloadData()
	}

	private fun downloadData() {
		val retrofit = Retrofit.Builder()
			.baseUrl("https://raw.githubusercontent.com/")
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(ProductAPI::class.java)
		li
	}

}