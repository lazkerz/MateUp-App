package com.example.mateup.data.remote

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.apps_magang.BuildConfig.API_BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig private constructor() {

    companion object {
        inline fun getApiService(context: Context, apiType: String): Any? {
            val chuckerInterceptor = ChuckerInterceptor.Builder(context)
                .collector(ChuckerCollector(context))
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(false)
                .build()

//            val API_BASE_URL = "https://makeup-api.herokuapp.com/"

            val retrofit = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val client = OkHttpClient.Builder()
                .addInterceptor(chuckerInterceptor)
                .build()

            return when (apiType) {
                "product" -> {
                    retrofit.newBuilder().client(client).build().create(ApiServiceProductTags::class.java)
                }
                "productBy" -> {
                    retrofit.newBuilder().client(client).build().create(ApiServicePersonalized::class.java)
                }
                "productTag" -> {
                    retrofit.newBuilder().client(client).build().create(ApiServiceProductType::class.java)
                }
                else -> throw IllegalArgumentException("Unknown API type: $apiType")
            }
        }

    }
}