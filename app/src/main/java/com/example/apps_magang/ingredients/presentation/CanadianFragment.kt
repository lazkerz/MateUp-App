package com.example.apps_magang.ingredients.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apps_magang.R
import com.example.apps_magang.auth.presenter.UserPresenter
import com.example.apps_magang.core.domain.Product
import com.example.apps_magang.core.utils.RealmManager
import com.example.apps_magang.core.utils.ResultState
import com.example.apps_magang.core.utils.SpacesItemDecoration
import com.example.apps_magang.core.view.ProductView
import com.example.apps_magang.dashboard.presentation.presenter.PersonalizedPresenter
import com.example.apps_magang.ingredients.adapter.IngredientsAdapter
import com.example.apps_magang.ingredients.presentation.presenter.ProductTagPresenter
import com.example.mateup.data.remote.ApiConfig
import com.example.mateup.data.remote.ApiServicePersonalized
import com.example.mateup.data.remote.ApiServiceProductTags
import io.realm.Realm

class CanadianFragment : Fragment(), ProductView {

    private lateinit var presenter: ProductTagPresenter
    private lateinit var adapter:  IngredientsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Realm.init(requireContext())
        RealmManager.initRealm()

        adapter = IngredientsAdapter(requireContext())

        val apiServiceProduct =
            ApiConfig.getApiService(requireContext(), "product") as? ApiServiceProductTags
        Log.d("ApiServiceProduct", "ApiServiceProduct is not null: $apiServiceProduct")

        apiServiceProduct?.let {
            presenter = ProductTagPresenter(it, this)
            presenter.getProductIngredient("Canadian")
            presenter.retrieveProductTagFromRealm()
        } ?: Log.e("CanadianFragment", "Failed to initialize ApiServiceProduct")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_canadian, container, false)

        var recyclerView = view.findViewById<RecyclerView>(R.id.rv_canadian)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(SpacesItemDecoration(3))

        recyclerView.adapter = adapter

        return view
    }

    override fun displayProduct(result: ResultState<List<Product>>) {
        when (result) {
            is ResultState.Success -> {
                // Handle data berhasil diterima
                val productData = result.data
                adapter.updateData(productData)
            }
            is ResultState.Error -> {
                // Handle jika terjadi error
                val errorMessage = result.error
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
            is ResultState.Loading -> {
                // Handle loading state
                Toast.makeText(requireContext(), "Loading..", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {

    }
}