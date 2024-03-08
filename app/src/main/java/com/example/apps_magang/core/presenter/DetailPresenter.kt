package com.example.apps_magang.core.presenter

import com.example.apps_magang.core.domain.Product
import io.realm.Realm

class DetailPresenter {
    fun getDataByIdFromRealm(uniqueId: Int): Product? {
        val realm = Realm.getDefaultInstance()
        return realm.where(Product::class.java).equalTo("id", uniqueId).findFirst()
    }
}
