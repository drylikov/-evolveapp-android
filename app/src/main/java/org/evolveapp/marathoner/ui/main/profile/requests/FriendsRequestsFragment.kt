package org.evolveapp.marathoner.ui.main.profile.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.FragmentFriendsRequestsBinding


class FriendsRequestsFragment : Fragment() {

    private var binding: FragmentFriendsRequestsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends_requests, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.back?.setOnClickListener { findNavController().navigateUp() }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}