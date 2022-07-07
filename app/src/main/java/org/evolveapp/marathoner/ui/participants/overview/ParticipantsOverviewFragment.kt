package org.evolveapp.marathoner.ui.participants.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import org.evolveapp.domain.models.marathon.participants.MarathonParticipants
import org.evolveapp.domain.models.marathon.participants.Participant
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.FragmentParticipantsOverviewBinding
import org.evolveapp.marathoner.ui.invite.InviteActivity
import org.evolveapp.marathoner.ui.main.profile.ProfileUtils
import org.evolveapp.marathoner.ui.participants.ParticipantsViewModel
import org.evolveapp.marathoner.utils.*

@AndroidEntryPoint
class ParticipantsOverviewFragment : Fragment() {

    private val viewModel by viewModels<ParticipantsViewModel>(ownerProducer = { requireActivity() })

    private lateinit var friendsAdapter: ParticipantsAdapter

    private lateinit var participantsAdapter: ParticipantsAdapter

    private var binding: FragmentParticipantsOverviewBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_participants_overview,
            container,
            false
        )

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.add?.setOnClickListener { goTo(InviteActivity::class.java) }
        binding?.back?.setOnClickListener { requireActivity().onBackPressed() }

        collectParticipants(loading = true)

    }

    private fun collectParticipants(loading: Boolean = true) {
        launchViewLifecycleScope {

            if (loading) {
                binding?.content?.gone()
                binding?.shimmer?.apply {
                    show()
                    showShimmer(true)
                }
            }


            viewModel.getParticipants(viewModel.marathonId ?: "").collect { apiResponse ->

                apiResponse.onSuccess { participants ->
                    updateParticipantsUI(participants)

                    if (loading) {
                        binding?.content?.show()
                        binding?.shimmer?.apply {
                            gone()
                            showShimmer(false)
                        }
                    }

                }

                apiResponse.onFailure { message, code -> handleApiErrorUI(message, code) }

            }
        }
    }

    private fun updateParticipantsUI(participants: MarathonParticipants) {

        updateFriendsUI(participants.friends)

        updateParticipantsUI(participants.participants)

    }

    private fun updateFriendsUI(friends: List<Participant>?) {

        if (friends.isNullOrEmpty()) {

            binding?.layoutInviteFriends?.show()

            binding?.cardUser?.btnAdd?.gone()

            binding?.cardUser?.name?.text = ProfileUtils.buildUserName(
                firstName = prefDao.firstName,
                lastName = prefDao.lastName
            )


            Glide.with(this).load(prefDao.userPhoto)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding?.cardUser?.photo!!)


            binding?.labelFriends?.gone()
            binding?.friends?.gone()

        } else {

            binding?.layoutInviteFriends?.gone()

            binding?.labelFriends?.apply {
                text = getString(R.string.x_friends, friends.size)
                show()
            }

            binding?.friends?.show()


            friendsAdapter = ParticipantsAdapter(participants = friends, isFriends = true)
            binding?.friends?.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = friendsAdapter
            }

        }

    }

    private fun updateParticipantsUI(participants: List<Participant>?) {


        if (participants.isNullOrEmpty()) {

            binding?.labelParticipantsCount?.gone()
            binding?.participants?.gone()

        } else {


            binding?.labelParticipantsCount?.show()
            binding?.participants?.show()

            binding?.labelParticipantsCount?.text =
                getString(R.string.other_participants_x, participants.size)

            participantsAdapter = ParticipantsAdapter(
                participants = participants,
                isFriends = false,
                onAddFriendClick = onAddFriendClick
            )

            binding?.participants?.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = participantsAdapter
            }

        }


    }

    private fun handleApiErrorUI(message: String? = null, code: Int? = null) {

        binding?.retry?.apply {
            layoutRetry.show()
            btnRetry.setOnClickListener {
                root.gone()
                collectParticipants(loading = true)
            }
        }

    }

    private val onAddFriendClick = OnClickWithResult<Participant, Boolean> { participant, _ ->

        val response = viewModel.sendFriendRequest(participant.user?.id.toString()).first()

        response.success
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding?.friends?.adapter = null
        binding?.participants?.adapter = null
        binding = null
    }

}