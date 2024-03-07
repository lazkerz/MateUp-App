//package com.example.apps_magang.dashboard.adapter
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.apps_magang.R
//
//class CarouselAdapter(
//    private val items: List<Int>,
//    private val context: Context
//) : RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {
//
//    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
//        private val ivCarousel: ImageView = view.findViewById(R.id.ivCarousel)
//
//        fun bind(data: Int) {
//            Glide.with(view)
//                .load(data)
//                .into(ivCarousel)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(
//            R.layout.item_carousel,
//            parent,
//            false
//        )
//        return ViewHolder(itemView)
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(items[position])
//    }
//}
//
