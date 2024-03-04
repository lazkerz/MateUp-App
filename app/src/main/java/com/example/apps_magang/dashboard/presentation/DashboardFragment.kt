package com.example.apps_magang.dashboard.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.apps_magang.R

class DashboardFragment : Fragment() {

    var viewPager = view?.findViewById<ViewPager2>(R.id.vp_carousel)
    var rv = view?.findViewById<RecyclerView>(R.id.rv_eyeshadow)
    var rv2 = view?.findViewById<RecyclerView>(R.id.rv_lipstick)
    var rv3 = view?.findViewById<RecyclerView>(R.id.rv_foundation)
    var rv4 = view?.findViewById<RecyclerView>(R.id.rv_mascara)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

//    private fun initializeComponent() {
//        rv = requireView().findViewById(R.id.rv_eyeshadow)
//        rv.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        rv.addItemDecoration(SpacesItemDecoration(6))
//    }
}