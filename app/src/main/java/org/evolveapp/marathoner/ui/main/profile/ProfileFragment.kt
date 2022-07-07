package org.evolveapp.marathoner.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import org.evolveapp.domain.models.friends.Friend
import org.evolveapp.domain.models.profile.Profile
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.FragmentProfileBinding
import org.evolveapp.marathoner.ui.admin.marathons.create.CreateMarathonActivity
import org.evolveapp.marathoner.utils.*

// TODO: 9/28/2021 Add loading animations to the screen
// TODO: 9/28/2021 Handle marathons invitations UI - WAITING FOR API
// TODO: 9/28/2021 Handle past marathons UI - WAITING FOR API
// TODO: 9/28/2021 Handle marathons you manage UI - WAITING FOR API


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel by viewModels<ProfileViewModel>()

    private var binding: FragmentProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // init UI with cached data until
        updateProfileUI(
            Profile(
                id = prefDao.userId?.toIntOrNull(),
                username = prefDao.username,
                email = prefDao.userEmail,
                firstName = prefDao.firstName,
                lastName = prefDao.lastName,
                avatar = prefDao.userPhoto,
                country = prefDao.country,
                dateOfBirthday = prefDao.dateOfBirth,
            )
        )

        initListeners()
        collectProfileInfo()
        collectFriends()
        collectFriendsRequests()

    }

    private fun initListeners() {
        binding?.settings?.setOnClickListener { navigate(R.id.settingsFragment) }
        binding?.marathonsInvitations?.setOnClickListener { navigate(R.id.marathonsInvitationsFragment) }
        binding?.friends?.setOnClickListener { navigate(R.id.friendsFragment) }
        binding?.crateMarathon?.setOnClickListener { goTo(CreateMarathonActivity::class.java) }
    }

    private fun collectProfileInfo() {
        launchViewLifecycleScope {
            viewModel.getProfile().first()
                .onSuccess { profile: Profile -> updateProfileUI(profile) }
                .onFailure { message, code ->
                    // TODO: 9/27/2021
                }
        }
    }

    private fun collectFriends() {
        launchViewLifecycleScope {
            viewModel.getFriends().first()
                .onSuccess { friends -> updateFriendsCardUI(friends) }
                .onFailure { message, code ->
                    // TODO: 9/28/2021
                }
        }
    }

    private fun collectFriendsRequests() {
        launchViewLifecycleScope {
            viewModel.getFriendsRequests().first()
                .onSuccess { friendsRequests -> updateFriendsRequestsNumberUI(friendsRequests.size) }
                .onFailure { message, code ->
                    // TODO: 9/28/2021
                }
        }
    }

    private fun updateFriendsRequestsNumberUI(number: Int) {

        if (number == 0) {
            binding?.friendsRequests?.gone()
        } else {
            binding?.friendsRequests?.apply {
                text = number.toString()
                show()
            }

        }

    }

    private fun updateFriendsCardUI(friends: List<Friend>) {

        val friendsNumber = friends.size

        binding?.friendsNumber?.text = getString(R.string.friends_x, friendsNumber)

        // TODO: 9/28/2021  handle friendsOverlap

    }

    private fun updateProfileUI(profile: Profile) {

        updateUserPhotoUI(profile.avatar)

        updateBioUI(country = profile.country, birthDate = profile.dateOfBirthday)

        binding?.name?.text = ProfileUtils.buildUserName(
            firstName = profile.firstName,
            lastName = profile.lastName
        )

        binding?.email?.text = profile.email
        // TODO: 9/27/2021

    }

    private fun updateBioUI(country: String?, birthDate: String?) {

        val displayCountryWithLogo = ProfileUtils.getDisplayCountryWithLogo(country ?: "")

        binding?.bio?.text = buildString {

            append(displayCountryWithLogo)

            birthDate?.apply {
                append(" • ")
                append(birthDate)
            }

        }

    }

    private fun updateUserPhotoUI(url: String?) {

        Glide.with(requireContext()).load(url)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.ic_profile_userpic)
            .error(R.drawable.ic_profile_userpic)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding!!.photo)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}