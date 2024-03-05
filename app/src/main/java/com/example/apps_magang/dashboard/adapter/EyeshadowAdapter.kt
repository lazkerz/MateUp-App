package com.example.apps_magang.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apps_magang.R
import com.example.apps_magang.core.domain.Product
import com.example.apps_magang.core.utils.fromJson
import com.example.apps_magang.core.utils.setImageFromUrl
import io.realm.RealmList

class EyeshadowAdapter (
    private val context: Context
) : RecyclerView.Adapter<EyeshadowAdapter.ViewHolder>() {

    private val list: MutableList<Product> = mutableListOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvProduct: TextView = itemView.findViewById(R.id.tvProduct)
        var imgRecommendation: ImageView = itemView.findViewById(R.id.imgRecommendation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(
            R.layout.item_recommendation,
            parent,
            false
        )
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.tvProduct.text = item?.name ?: ""

//        val symbol = null
//        if (symbol != null) {
//            val image = symbol?.imageLink
//            Log.d("MarketCapAdapter", "Symbol LOGOURL: $image")
//
//            if (!image.isNullOrEmpty()) {
//                Log.d("MarketCapAdapter", "Trying to load image from URL: $image")
//                holder.imgRecommendation.setImageFromUrl(context, image)
//                Log.d("MarketCapAdapter", "Image URL: $image")
//            } else {
//                Log.d("MarketCapAdapter", "Image URL is empty or null.")
//            }
//        } else {
//            Log.d("MarketCapAdapter", "Symbol is null.")
//        }

        Glide.with(context)
            .load(item.imageLink)
            .placeholder(R.drawable.image_loading_placeholder)
            .error(R.drawable.image_load_error)
            .into(holder.imgRecommendation)
    }

    fun updateData(newList: List<Product>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}