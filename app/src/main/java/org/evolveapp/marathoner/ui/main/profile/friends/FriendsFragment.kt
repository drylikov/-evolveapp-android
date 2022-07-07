package org.evolveapp.marathoner.ui.main.profile.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import org.evolveapp.domain.models.friends.Friend
import org.evolveapp.domain.models.friends.FriendRequest
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.FragmentFriendsBinding
import org.evolveapp.marathoner.utils.*

@AndroidEntryPoint
class FriendsFragment : Fragment() {

    private var friendsAdapter: FriendsAdapter? = null

    private val viewModel by viewModels<FriendsViewModel>()

    private var binding: FragmentFriendsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        init()

    }

    private fun init() {

        // in case of back from search friends screen
        (requireActivity() as AppCompatActivity).hideSoftKeyboard()

        binding?.shimmer?.apply {
            show()
            showShimmer(true)
        }

        collectFriends()
        collectFriendsRequests()

    }

    private fun collectFriends() {
        launchViewLifecycleScope {
            viewModel.getFriends().first()
                .onSuccess { friends -> updateFriendsUI(friends) }
                .onFailure { message, code ->

                    binding?.retry?.apply {
                        layoutRetry.show()
                        btnRetry.setOnClickListener {
                            root.gone()
                            init()
                        }
                    }

                }
        }
    }

    private fun collectFriendsRequests() {
        launchViewLifecycleScope {
            viewModel.getFriendsRequests().first()
                .onSuccess { friendsRequests -> updateFriendsRequestsUI(friendsRequests) }
                .onFailure { message, code ->
                    // TODO: 9/28/2021
                }
        }
    }

    private fun updateFriendsRequestsUI(friendsRequests: List<FriendRequest>) {

        if (friendsRequests.isEmpty()) {
            binding?.requests?.gone()
            binding?.gap?.gone()
        } else {
            binding?.requests?.show()
            binding?.gap?.show()
        }


    }

    private fun initFriendsListUI(friends: List<Friend>) {

        friendsAdapter = FriendsAdapter(friends)

        binding?.friends?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = friendsAdapter
        }
    }

    private fun updateActionBarTitle(title: String) {
        binding?.title?.text = title
    }

    private fun updateFriendsUI(friends: List<Friend>) {

        val updatedActionBarTitle = getString(R.string.friends_x, friends.size)
        updateActionBarTitle(updatedActionBarTitle)

        if (friends.isEmpty()) {
            binding?.layoutFindFriends?.root?.show()


            Glide.with(requireContext()).load(prefDao.userPhoto)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_profile_userpic)
                .error(R.drawable.ic_profile_userpic)
                .into(binding!!.layoutFindFriends.profilePic)

        } else {
            binding?.layoutFindFriends?.root?.gone()
            initFriendsListUI(friends)
        }


        binding?.shimmer?.apply {
            gone()
            showShimmer(false)
        }

    }

    private fun initListeners() {

        binding?.back?.setOnClickListener { findNavController().navigateUp() }
        binding?.requests?.setOnClickListener { navigate(R.id.friendsRequestsFragment) }
        binding?.cardSearch?.setOnClickListener { navigate(R.id.searchFriendsFragment) }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.friends?.adapter = null
        binding = null
        friendsAdapter = null
    }

}