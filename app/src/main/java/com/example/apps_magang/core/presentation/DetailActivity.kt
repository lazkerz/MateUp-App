package com.example.apps_magang.core.presentation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apps_magang.R
import com.example.apps_magang.core.adapter.ShadeAdapter
import com.example.apps_magang.core.domain.Product
import com.example.apps_magang.core.domain.ProductColor
import com.example.apps_magang.core.presenter.DetailPresenter
import com.example.apps_magang.core.utils.RealmManager
import com.example.apps_magang.core.utils.ResultState
import com.example.apps_magang.core.utils.SpacesItemDecoration
import com.example.apps_magang.core.view.ProductView
import io.realm.Realm

class DetailActivity : AppCompatActivity(), ProductView {

    private lateinit var adapter: ShadeAdapter
    private lateinit var rvShade: RecyclerView
    private lateinit var presenter: DetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        Realm.init(this)
        RealmManager.initRealm()

        rvShade = findViewById(R.id.rv_shade)
        adapter = ShadeAdapter(this)
        initRecyclerView(adapter, rvShade)

        presenter = DetailPresenter()
        val uniqueId = intent.getIntExtra("id", 0)
        val dataItem = presenter.getDataByIdFromRealm(uniqueId)

        val tvBrand = findViewById<TextView>(R.id.tv_brand_detail)
        tvBrand.text = dataItem?.brand ?:""

        val tvCategory = findViewById<TextView>(R.id.tv_category)
        tvCategory.text = dataItem?.productType ?:""

        val tvProduct = findViewById<TextView>(R.id.tv_name_product)
        tvProduct.text = dataItem?.name ?: ""

        val tvPrice = findViewById<TextView>(R.id.tv_price_product)
        tvPrice.text = dataItem?.price ?:""

        val tvDesc = findViewById<TextView>(R.id.tv_desc)
        tvDesc.text = dataItem?.description ?: ""

        val tvLink = findViewById<TextView>(R.id.tv_link_product)
        tvLink.text = dataItem?.productLink ?: ""

        var imgProduct = findViewById<ImageView>(R.id.iv_img_product)

        Glide.with(this)
            .load(dataItem?.imageLink)
            .placeholder(R.drawable.image_loading_placeholder)
            .error(R.drawable.image_load_error)
            .into(imgProduct)
    }

    private fun initRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
        val spanCount = 5
        recyclerView.layoutManager = GridLayoutManager(this, spanCount, GridLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SpacesItemDecoration(6))
    }

    override fun displayProduct(result: ResultState<List<Product>>) {
        when (result) {
            is ResultState.Success -> {
                // Handle data berhasil diterima
                val productData = result.data
                val colorList = mutableListOf<ProductColor>()

                // Ambil semua warna dari setiap produk
                for (product in productData) {
                    val colors = product.productColors
                    Log.d("DetailActivity", "Product ID: ${product.id}, Number of Colors: ${colors?.size}")
                    if (colors != null && colors.isNotEmpty()) {
                        colorList.addAll(colors)
                    }
                }

                // Tambahkan log untuk memeriksa apakah ProductColor berhasil diambil
                Log.d("DetailActivity", "Number of ProductColor items: ${colorList.size}")

                adapter.updateData(colorList)
            }
            is ResultState.Error -> {
                // Handle jika terjadi error
                val errorMessage = result.error
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
            is ResultState.Loading -> {
                // Handle loading state
                Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
