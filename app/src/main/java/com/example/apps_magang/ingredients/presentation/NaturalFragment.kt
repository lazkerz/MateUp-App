package com.example.apps_magang.ingredients.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.apps_magang.R
import com.example.apps_magang.core.domain.Product
import com.example.apps_magang.core.presentation.DetailActivity
import com.example.apps_magang.core.utils.RealmManager
import com.example.apps_magang.core.utils.ResultState
import com.example.apps_magang.core.utils.SpacesItemDecoration
import com.example.apps_magang.core.view.ProductView
import com.example.apps_magang.ingredients.adapter.IngredientsAdapter
import com.example.apps_magang.ingredients.presentation.presenter.ProductTagPresenter
import com.example.apps_magang.product.presenter.ProductTypePresenter
import com.example.mateup.data.remote.ApiConfig
import com.example.mateup.data.remote.ApiServiceProductTags
import io.realm.Realm


class NaturalFragment : Fragment(), ProductView {

    private lateinit var presenter: ProductTagPresenter
    private lateinit var adapter: IngredientsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Realm.init(requireContext())
        RealmManager.initRealm()

        adapter = IngredientsAdapter(requireContext(),  object : IngredientsAdapter.OnItemClickListener{
            override fun onItemClick(data: Product) {
                val productId = data.id ?: ""
                Log.d("MainActivity", "Product ID clicked: $productId")

                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("id", productId)
                startActivity(intent)
            }
        })

        val apiServiceProduct =
            ApiConfig.getApiService(requireContext(), "product") as? ApiServiceProductTags
        Log.d("ApiServiceProduct", "ApiServiceProduct is not null: $apiServiceProduct")

        apiServiceProduct?.let {
            presenter = ProductTagPresenter(it, this)
            getContent()
        } ?: Log.e("CanadianFragment", "Failed to initialize ApiServiceProduct")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_natural, container, false)

        var recyclerView = view.findViewById<RecyclerView>(R.id.rv_natural)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(SpacesItemDecoration(3))

        recyclerView.adapter = adapter

        setLoading(adapter.itemCount == 0)

        val refresh =  view.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
        refresh.setOnRefreshListener{
            getContent()
            if (refresh.isRefreshing) {
                refresh.isRefreshing = false
            }
        }

        return view
    }

    private fun getContent(){
        presenter.getProductIngredient("Natural")
        presenter.retrieveProductTagFromRealm()
    }

    private fun setLoading(isLoading: Boolean) {
        val viewLoading = view?.findViewById<RelativeLayout>(R.id.view_loading)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.rv_natural)

        if (isLoading) {
            recyclerView?.postDelayed({
                viewLoading?.visibility = View.VISIBLE
                recyclerView.visibility = View.INVISIBLE
            }, 200)
        } else {
            viewLoading?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
        }
    }

    override fun displayProduct(result: ResultState<List<Product>>) {
        when (result) {
            is ResultState.Success -> {
                // Handle data berhasil diterima
                val productData = result.data
                adapter.updateData(productData)
                setLoading(productData.isEmpty())
            }
            is ResultState.Error -> {
                // Handle jika terjadi error
                val errorMessage = result.error
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
            is ResultState.Loading -> {
                // Handle loading state
                setLoading(true)
                Toast.makeText(requireContext(), "Loading..", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {

    }
}