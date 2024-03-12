package com.example.apps_magang.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.apps_magang.R
import com.example.apps_magang.dashboard.presenter.slider

class CarouselAdapter(private val slides: List<slider>) :
    RecyclerView.Adapter<CarouselAdapter.slideViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): slideViewHolder {
        return slideViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slider_item_container,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return slides.size
    }

    override fun onBindViewHolder(holder: slideViewHolder, position: Int) {
        holder.bind(slides[position])
    }

    inner class slideViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imageIcon = view.findViewById<ImageView>(R.id.imageSlider)

        fun bind(slide: slider) {
            imageIcon.setImageResource(slide.Icon)
        }
    }
}