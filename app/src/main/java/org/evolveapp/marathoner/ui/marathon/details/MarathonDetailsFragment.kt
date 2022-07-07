package org.evolveapp.marathoner.ui.marathon.details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.FragmentMarathonDetailsBinding
import org.evolveapp.marathoner.ui.marathon.details.assignments.AssignmentsFragment
import org.evolveapp.marathoner.ui.marathon.details.discussion.DiscussionFragment
import org.evolveapp.marathoner.ui.marathon.details.files.FilesFragment
import org.evolveapp.marathoner.ui.marathon.details.timeline.TimelineFragment


class MarathonDetailsFragment : Fragment() {

    private var binding: FragmentMarathonDetailsBinding? = null

    private var tabsAdapter: TabsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_marathon_details, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayoutWithViewPager(getTabsList())

    }

    private fun getTabsList(): List<Tab> {

        return listOf(
            Tab("Timeline", TimelineFragment()),
            Tab("Assignments", AssignmentsFragment.newInstance("", "")),
            Tab("Discussion", DiscussionFragment.newInstance("", "")),
            Tab("Files", FilesFragment.newInstance("", "")),
        )
    }

    private fun setupTabLayoutWithViewPager(tabs: List<Tab>) {

        tabsAdapter = TabsAdapter(tabs, childFragmentManager, viewLifecycleOwner.lifecycle)

        binding?.pager?.adapter = tabsAdapter

        binding?.tabLayout?.setTabTextColors(Color.parseColor("#727272"), Color.BLACK)

        TabLayoutMediator(binding?.tabLayout!!, binding?.pager!!) { tab, position ->
            tab.text = tabs[position].name
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding?.pager?.adapter = null
        tabsAdapter = null
        binding = null

    }

}