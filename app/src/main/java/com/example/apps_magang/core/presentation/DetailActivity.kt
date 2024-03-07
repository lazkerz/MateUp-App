package com.example.apps_magang.core.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apps_magang.R
import com.example.apps_magang.core.adapter.ShadeAdapter
import com.example.apps_magang.core.domain.Product
import com.example.apps_magang.core.utils.RealmManager
import com.example.apps_magang.core.utils.ResultState
import com.example.apps_magang.core.utils.SpacesItemDecoration
import com.example.apps_magang.core.view.ProductView
import com.example.mateup.data.remote.ApiConfig
import com.example.mateup.data.remote.ApiServicePersonalized
import io.realm.Realm

class DetailActivity : AppCompatActivity(), ProductView {


    private lateinit var shadeAdapter: ShadeAdapter
    private lateinit var rvShade: RecyclerView
    private lateinit var domain: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        Realm.init(this )
        RealmManager.initRealm()

        val apiService = ApiConfig.getApiService(this)

        domain = Product(
            apiService,
            this
        )

        // Inisialisasi adapter dan set ke RecyclerView
        shadeAdapter = ShadeAdapter(this)

        val data = ApiServicePersonalized()

        if (data != null){

            val  tvNama = findViewById<TextView>(R.id.tvNama)
            tvNama.text = dataItem.coinInfo?.name ?: ""

            val  tvFullname = findViewById<TextView>(R.id.tvFullnama)
            tvFullname.text = dataItem.coinInfo?.fullName ?: ""

            val  tvPrice = findViewById<TextView>(R.id.tvPrice)
            tvPrice.text = dataItem.dISPLAY?.uSD?.pRICE ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_detail, container, false)

        rvShade = view.findViewById(R.id.rv_shade)

        initRecyclerView(ShadeAdapter, rvShade)

        return view
    }

    private fun initRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
        recyclerView.layoutManager = GridLayoutManager(this , GridLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SpacesItemDecoration(6))
    }

    override fun displayProduct(result: ResultState<List<Product>>) {
        TODO("Not yet implemented")
    }
}