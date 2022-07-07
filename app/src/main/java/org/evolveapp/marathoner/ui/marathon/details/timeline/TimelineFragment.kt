package org.evolveapp.marathoner.ui.marathon.details.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.FragmentTimelineBinding
import org.evolveapp.marathoner.ui.admin.marathon.posts.add.AddPostActivity
import org.evolveapp.marathoner.ui.marathon.MarathonViewModel


@AndroidEntryPoint
class TimelineFragment : Fragment() {

    private val viewModel by viewModels<MarathonViewModel>(ownerProducer = { requireActivity() })

    private var binding: FragmentTimelineBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timeline, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.newPost?.setOnClickListener {
            AddPostActivity.launch(requireActivity(), viewModel.marathonId ?: "")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null

    }


}