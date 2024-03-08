package com.example.mateup.data.remote

import com.example.apps_magang.core.domain.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceProductTags {
        @GET("api/v1/products.json")
        fun getProductTags(
                @Query("product_tags") product_tags: String,
        ): Call<List<Product>>
}

interface ApiServicePersonalized {
        @GET("api/v1/products.json")
        fun getPersonalized(
                @Query("product_tags") product_tags: String,
                @Query("product_type") product_type: String,
        ): Call<List<Product>>
}

interface ApiServiceProductType {
        @GET("api/v1/products.json")
        fun getProductType(
                @Query("product_type") product_type: String,
                @Query("product_category") category: String
        ): Call<List<Product>>
}