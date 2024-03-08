package com.example.apps_magang.core.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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

        holder.tvHex.text = item.colourName ?: "Unknown"

        val backgroundValue = item.hexValue ?: ""
        holder.imgShade.background = ColorDrawable(Color.parseColor(backgroundValue))
    }


    fun getColorFromHex(hexValue: String): Int {
        return try {
            Color.parseColor(hexValue)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            // Handle jika terjadi kesalahan dalam parsing warna
            Color.LTGRAY // Ganti dengan warna default jika parsing gagal
        }
    }


    fun updateData(newList: List<ProductColor>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    // Tambahkan satu data ke adapter
    fun addData(product: ProductColor) {
        list.add(product)
        notifyItemInserted(list.size - 1)
    }
}
