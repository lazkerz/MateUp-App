package com.example.apps_magang.ingredients.presentation.presenter

import android.util.Log
import com.example.apps_magang.core.domain.Product
import com.example.apps_magang.core.view.ProductView
import com.example.apps_magang.core.utils.ResultState
import com.example.mateup.data.remote.ApiServiceProductTags
import io.realm.Realm
import io.realm.RealmList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

class ProductTagPresenter(
    private val apiIngredient: ApiServiceProductTags,
    private val view: ProductView
){
    fun getProductIngredient(
        tags: String,
    ) {
        try {
            val call = apiIngredient.getProductTags(tags)

            call.enqueue(object : Callback<List<Product>> {
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (response.isSuccessful) {
                        val products = response.body()
                        products?.let {
                            for (product in products) {
                                saveProductTagToRealm(product)
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
    fun saveProductTagToRealm(dataItem: Product) {
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

    fun getProductTagByIdFromRealm(uniqueId: String): Product? {
        val realm = Realm.getDefaultInstance()
        return realm.where(Product::class.java).equalTo("product.id", uniqueId).findFirst()
    }

    fun retrieveProductTagFromRealm() {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(Product::class.java).findAll()

        if (result.isNotEmpty()) {
            val items = RealmList<Product>().apply {
                Collections.addAll(realm.copyFromRealm(result))
            }
            view.displayProduct(ResultState.Success(items))
        } else {
            view.displayProduct(ResultState.Error("No data in Realm"))
        }

        realm.close()
    }
}