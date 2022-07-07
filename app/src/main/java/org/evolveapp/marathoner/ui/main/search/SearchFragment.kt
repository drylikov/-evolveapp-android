package org.evolveapp.marathoner.ui.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.FragmentSearchBinding
import org.evolveapp.marathoner.ui.main.search.filter.FilterFragment
import org.evolveapp.marathoner.utils.simulateLoadingData


class SearchFragment : Fragment() {

    private var binding: FragmentSearchBinding? = null

    private var categoriesAdapter: CategoriesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCategoriesUI(getDummyCategories())

        binding?.filter?.setOnClickListener { showFilterBottomSheet() }


        simulateLoadingData(
            shimmerLayout = binding?.shimmerMarathons,
            content = binding?.content
        )
        simulateLoadingData(
            shimmerLayout = binding?.shimmerCategories,
            content = binding?.categoriesLayout
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.categories?.adapter = null
        binding = null
        categoriesAdapter = null
    }

    private fun initCategoriesUI(categories: List<Category>) {

        categoriesAdapter = CategoriesAdapter(categories)

        binding?.categories?.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoriesAdapter
        }

    }

    private fun getDummyCategories(): List<Category> = listOf(
        Category("Sports"),
        Category("Fun"),
        Category("Olympic"),
        Category("General"),
        Category("Private"),
        Category("Sports"),
        Category("Fun"),
        Category("Olympic"),
        Category("General"),
        Category("Private"),
    )

    private fun showFilterBottomSheet() {

        FilterFragment.newInstance("", "").show(childFragmentManager, "")

    }

}