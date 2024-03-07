package com.example.apps_magang.core.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apps_magang.R
import com.example.apps_magang.core.domain.ProductColor

class ShadeAdapter (
    private val context: Context
) : RecyclerView.Adapter<ShadeAdapter.ViewHolder>() {

    private val list: MutableList<ProductColor> = mutableListOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvHex: TextView = itemView.findViewById(R.id.tvHex)
        var imgShade: View = itemView.findViewById(R.id.imgShade)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(
            R.layout.item_shade,
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

        holder.tvHex.text = item?.colourName ?: ""

        val defaultColor: Int = Color.parseColor("#808080")/* Default color code jika hexValue null atau tidak valid */
        // Jika item tidak null dan hexValue tidak null atau kosong, konversi hexValue ke integer
        val color: Int = item?.hexValue?.let { Color.parseColor(it) } ?: defaultColor
        holder.imgShade.setBackgroundColor(color)
    }

    fun updateData(newList: List<ProductColor>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun addData(product: ProductColor) {
        list.clear()
        list.add(product)
        notifyDataSetChanged()
    }

}
