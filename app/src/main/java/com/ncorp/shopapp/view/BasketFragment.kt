package com.ncorp.shopapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ncorp.shopapp.databinding.FragmentBasketBinding
import com.ncorp.shopapp.model.Product
import com.ncorp.shopapp.viewmodel.ProductViewModel

// Sepet ekranı Fragment’ı
class BasketFragment : Fragment() {
	private var _binding: FragmentBasketBinding? = null
	private val binding get() = _binding!!

	// ViewModel'i Activity scope’unda paylaşıyoruz
	private val productViewModel: ProductViewModel by activityViewModels()

	// Adapter late init, onViewCreated’de initialize edilecek
	private lateinit var basketAdapter: BasketRecyclerAdapter

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentBasketBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// Başlangıçta boş listeyle adapter oluşturulur,
		// Sepetteki ürünlerin azalma işlemi tetiklendiğinde callback devreye girer
		basketAdapter = BasketRecyclerAdapter(arrayListOf()) { product, action ->
			when (action) {
				BasketRecyclerAdapter.ActionType.DECREASE ->
					productViewModel.decreaseProductCount(product) // Azaltma fonksiyonu çağrılır
			}
		}

		// RecyclerView için LinearLayoutManager atanır
		binding.basketRecyclerView.layoutManager = LinearLayoutManager(requireContext())
		binding.basketRecyclerView.setHasFixedSize(true)
		binding.basketRecyclerView.adapter = basketAdapter

		// Sepet listesindeki değişiklikleri gözle, adapter verisini güncelle
		productViewModel.basket.observe(viewLifecycleOwner) { basketList ->
			basketAdapter.basketList.clear()              // Eski listeyi temizle
			basketAdapter.basketList.addAll(basketList)   // Yeni veriyi ekle
			basketAdapter.notifyDataSetChanged()          // Liste değişti diye bildir
		}

		// Toplam tutarı gözle ve TextView'a yaz
		productViewModel.totalBasket.observe(viewLifecycleOwner) { total ->
			binding.totalPriceText.text = "Toplam: ₺ $total"
		}

		binding.checkoutButton.setOnClickListener {
			productViewModel.completeOrder()
			android.widget.Toast.makeText(requireContext(), "Siparişiniz başarıyla oluşturuldu!", android.widget.Toast.LENGTH_LONG).show()
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null // Bellek sızıntısı olmaması için binding null yapılır
	}
}
