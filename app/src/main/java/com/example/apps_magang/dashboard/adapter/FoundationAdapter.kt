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


class FoundationAdapter (
    private val context: Context,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<FoundationAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(data: Product)
    }

    private val list: MutableList<Product> = mutableListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvProduct: TextView = itemView.findViewById(R.id.tvProduct)
        var tvBrand: TextView = itemView.findViewById(R.id.tvBrand)
        var imgRecommendation: ImageView = itemView.findViewById(R.id.imgRecommendation)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(list[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(
            R.layout.eyeshadow_recommendation,
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
        holder.tvBrand.text = item.brand?.toUpperCase() ?: ""


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

    fun addData(product: Product) {
        list.add(product)
        notifyDataSetChanged()
    }

}
