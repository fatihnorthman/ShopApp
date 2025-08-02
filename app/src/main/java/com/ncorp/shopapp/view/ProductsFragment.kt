package com.ncorp.shopapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.ncorp.shopapp.databinding.FragmentProductsBinding
import com.ncorp.shopapp.model.Product
import com.ncorp.shopapp.viewmodel.ProductViewModel


class ProductsFragment : Fragment(), ProductRecyclerAdapter.Listener {
	private var _binding: FragmentProductsBinding? = null
	private val binding get() = _binding!!
	private val productViewModel: ProductViewModel by activityViewModels()
	private var productRecyclerAdapter: ProductRecyclerAdapter? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		_binding = FragmentProductsBinding.inflate(inflater, container, false)
		val view = binding.root
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
		productViewModel.downloadData()
		productViewModel.productList.observe(viewLifecycleOwner, Observer {
			productRecyclerAdapter = ProductRecyclerAdapter(it, this)
			binding.recyclerView.adapter = productRecyclerAdapter


		})


	}

	override fun onItemClick(product: Product) {
		productViewModel.addtoBasket(product)

	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

}


