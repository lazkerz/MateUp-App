package com.example.apps_magang.ingredients.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.apps_magang.ingredients.presentation.CanadianFragment
import com.example.apps_magang.ingredients.presentation.OrganicFragment
import com.example.apps_magang.ingredients.presentation.NaturalFragment
import com.example.apps_magang.ingredients.presentation.VeganFragment

class SectionPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> NaturalFragment()
            1 -> CanadianFragment()
            2 -> VeganFragment()
            3 -> OrganicFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

}