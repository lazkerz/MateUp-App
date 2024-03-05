package com.example.apps_magang.core.view

import com.example.apps_magang.core.domain.Product
import com.example.apps_magang.core.utils.ResultState

interface ProductView {
    fun displayProduct(result: ResultState<List<Product>>)
}