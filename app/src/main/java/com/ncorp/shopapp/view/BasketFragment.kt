package com.ncorp.shopapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ncorp.shopapp.databinding.FragmentBasketBinding
import com.ncorp.shopapp.model.Product
import com.ncorp.shopapp.viewmodel.ProductViewModel


class BasketFragment : Fragment() {
	private var _binding: FragmentBasketBinding? = null
	private val binding get() = _binding!!

	private val ProductViewModel: ProductViewModel by activityViewModels()
	private var basketAdapter: BasketRecyclerAdapter? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentBasketBinding.inflate(inflater, container, false)
		val view = binding.root
		return view


	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.basketRecyclerView.layoutManager = LinearLayoutManager(requireContext())
		ProductViewModel.basket.observe(viewLifecycleOwner) {
			basketAdapter = BasketRecyclerAdapter(it as ArrayList<Product>) { product, action ->
				when (action) {
					BasketRecyclerAdapter.ActionType.DELETE -> ProductViewModel.deleteProductFromBasket(
						product
					)

					BasketRecyclerAdapter.ActionType.DECREASE -> ProductViewModel.decreaseProductCount(
						product
					)
				}
				binding.basketRecyclerView.adapter = basketAdapter
			}
			ProductViewModel.totalBasket.observe(viewLifecycleOwner, Observer {
				binding.totalPriceText.text = "Toplam: ₺ ${it.toString()}"
			})


		}

	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null


	}

}