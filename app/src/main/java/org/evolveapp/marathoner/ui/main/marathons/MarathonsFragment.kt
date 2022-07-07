package org.evolveapp.marathoner.ui.main.marathons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.FragmentMarathonsBinding
import org.evolveapp.marathoner.ui.marathon.MarathonActivity
import org.evolveapp.marathoner.utils.hide
import org.evolveapp.marathoner.utils.show
import org.evolveapp.marathoner.utils.simulateLoadingData


class MarathonsFragment : Fragment() {

    private var binding: FragmentMarathonsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_marathons, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.cardMarathon?.container?.setOnClickListener {

            // FIXME: 10/6/2021
            MarathonActivity.launch(requireActivity(), "189")

        }


        binding?.marathonCount?.hide()
        simulateLoadingData(
            shimmerLayout = binding?.shimmer,
            content = binding?.layoutMarathons
        ) {
            binding?.marathonCount?.show()
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}