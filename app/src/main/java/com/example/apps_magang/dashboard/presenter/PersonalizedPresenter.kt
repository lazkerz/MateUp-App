package com.example.apps_magang.dashboard.presentation.presenter

import android.util.Log
import com.example.apps_magang.core.domain.Product
import com.example.apps_magang.core.view.ProductView
import com.example.apps_magang.core.utils.ResultState
import com.example.mateup.data.remote.ApiServicePersonalized
import io.realm.Realm
import io.realm.RealmList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PersonalizedPresenter (
    private val apiPersonalized : ApiServicePersonalized,
    private val view : ProductView
){
    fun getProductPersonalized(
        tags: String,
        productType: String,
    ) {
        try {
            val call = apiPersonalized.getPersonalized(tags, productType)

            call.enqueue(object : Callback<List<Product>> {
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (response.isSuccessful) {
                        val products = response.body()
                        products?.let {
                            for (product in products) {
                                saveProductToRealm(product)
                            }
                        }
                        view.displayProduct(ResultState.Success(products as List<Product>))
                        Log.d("Product", "Response: $response")
                    } else {
                        Log.e("Product", "Error: ${response.message()}")
                        view.displayProduct(ResultState.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    Log.e("Product", "Error: ${t.message}")
                    view.displayProduct(ResultState.Error(t.message.toString()))
                }
            })

        } catch (e: Exception) {
            Log.e("Product", "Error: ${e.message}")
            view.displayProduct(ResultState.Error(e.message.toString()))
        }
    }

    fun saveProductToRealm(dataItem: Product) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync(
            { backgroundRealm ->
                backgroundRealm.insertOrUpdate(dataItem)
            },
            {
                realm.close()
            },
            { error ->
                error.printStackTrace()
                realm.close()
            }
        )
    }

    fun getDataByIdFromRealm(uniqueId: String): Product? {
        val realm = Realm.getDefaultInstance()
        return realm.where(Product::class.java).equalTo("product.id", uniqueId).findFirst()
    }

    fun getProductFromRealm() {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(Product::class.java).findAll()

        if (result.isNotEmpty()) {
            val items = RealmList<Product>().apply {
                addAll(realm.copyFromRealm(result))
            }
            view.displayProduct(ResultState.Success(items))
        } else {
            view.displayProduct(ResultState.Error(""))
        }

        realm.close()
    }

}