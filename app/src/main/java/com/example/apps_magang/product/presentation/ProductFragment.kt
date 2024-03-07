package com.example.apps_magang.product.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.apps_magang.R
import com.example.apps_magang.ingredients.adapter.SectionPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProductFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPager(view)
    }

    private fun setViewPager(view: View) {
        val viewPagerAdapter =
            SectionPagerAdapter(childFragmentManager, lifecycle)
        viewPager = view.findViewById(R.id.view_pager)
        viewPager.adapter = viewPagerAdapter
        viewPager.isUserInputEnabled = false
        tabLayout = view.findViewById(R.id.tab_layout)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLE[position])
        }.attach()
    }

    companion object {
        private val TAB_TITLE = intArrayOf(
            R.string.lipstcik,
            R.string.base,
            R.string.eyeshadow,
            R.string.blush,
        )
    }
}