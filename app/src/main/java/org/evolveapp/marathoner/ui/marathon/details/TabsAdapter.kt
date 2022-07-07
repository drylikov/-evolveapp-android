package org.evolveapp.marathoner.ui.marathon.details

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabsAdapter(
    private val tabs: List<Tab>,
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = tabs.size

    override fun createFragment(position: Int) = tabs[position].fragment

}


data class Tab(
    val name: String,
    val fragment: Fragment
)