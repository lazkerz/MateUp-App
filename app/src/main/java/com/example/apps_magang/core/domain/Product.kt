package com.example.apps_magang.core.domain

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.io.Serializable
import com.google.gson.annotations.SerializedName


@RealmClass
open class Product: RealmObject(), Serializable {
    @PrimaryKey
    var id: Int = 0

    @SerializedName("brand")
    var brand: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("price")
    var price: String? = null

    @SerializedName("image_link")
    var imageLink: String? = null

    @SerializedName("product_link")
    var productLink: String? = null

    @SerializedName("website_link")
    var websiteLink: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("category")
    var category: String? = null

    @SerializedName("product_type")
    var productType: String? = null

    @SerializedName("tag_list")
    var tagList: RealmList<String>? = null

    @SerializedName("product_api_url")
    var productApiUrl: String? = null

    @SerializedName("api_featured_image")
    var apiFeaturedImage: String? = null

    @SerializedName("product_colors")
    var productColors: RealmList<ProductColor>? = null
}

@RealmClass
open class ProductColor : RealmObject() , Serializable {
    @SerializedName("hex_value")
    var hexValue: String? = null

    @SerializedName("colour_name")
    var colourName: String? = null
}