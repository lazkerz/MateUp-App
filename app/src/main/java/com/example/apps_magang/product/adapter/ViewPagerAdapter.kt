package com.example.apps_magang.product.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.apps_magang.ingredients.presentation.CanadianFragment
import com.example.apps_magang.ingredients.presentation.NaturalFragment
import com.example.apps_magang.ingredients.presentation.OrganicFragment
import com.example.apps_magang.ingredients.presentation.VeganFragment
import com.example.apps_magang.product.presentation.BaseFragment
import com.example.apps_magang.product.presentation.BlushFragment
import com.example.apps_magang.product.presentation.EyeshadowFragment
import com.example.apps_magang.product.presentation.LipstickFragment

class ViewPagerAdapter (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> LipstickFragment()
            1 -> BaseFragment()
            2 -> EyeshadowFragment()
            3 -> BlushFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

}