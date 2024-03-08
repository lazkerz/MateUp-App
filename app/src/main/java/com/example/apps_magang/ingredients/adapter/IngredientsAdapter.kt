package com.example.apps_magang.ingredients.adapter

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

class IngredientsAdapter (
    private val context: Context
) : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private val list: MutableList<Product> = mutableListOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        var tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvProductDesc: TextView = itemView.findViewById(R.id.tvProductDesc)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        var ivProduct: ImageView = itemView.findViewById(R.id.ivProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(
            R.layout.item_product,
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

        holder.tvCategory.text = item?.productType ?: ""
        holder.tvProductName.text = item?.name ?: ""
        holder.tvProductDesc.text = item?.description ?: ""
        holder.tvPrice.text = item?.price ?: ""


        Glide.with(context)
            .load(item.imageLink)
            .placeholder(R.drawable.image_loading_placeholder)
            .error(R.drawable.image_load_error)
            .into(holder.ivProduct)
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
