package org.evolveapp.marathoner.ui.participants.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.evolveapp.domain.models.marathon.participants.Participant
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.ListitemMarathonParticipantBinding
import org.evolveapp.marathoner.ui.main.profile.ProfileUtils
import org.evolveapp.marathoner.utils.OnClickWithResult
import org.evolveapp.marathoner.utils.gone
import org.evolveapp.marathoner.utils.prefDao
import org.evolveapp.marathoner.utils.show

class ParticipantsAdapter(
    private val participants: List<Participant>,
    /**
     * Making this variable = true will hiding all action buttons
     */
    private val isFriends: Boolean = false,
    var onAddFriendClick: OnClickWithResult<Participant, Boolean>? = null,
) : RecyclerView.Adapter<ParticipantsAdapter.Companion.ParticipantHolder>() {


    companion object {

        class ParticipantHolder(val binding: ListitemMarathonParticipantBinding) :
            RecyclerView.ViewHolder(binding.root)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantHolder {

        val binding: ListitemMarathonParticipantBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.listitem_marathon_participant,
            parent,
            false
        )

        return ParticipantHolder(binding)
    }

    override fun onBindViewHolder(holder: ParticipantHolder, position: Int) {
        val binding = holder.binding
        val participant = participants[position]
        val context = holder.itemView.context

        if (isFriends || (participant.user?.id.toString() == context.prefDao.userId)) {
            binding.btnAdd.gone()
        } else {
            binding.btnAdd.show()
        }


        val displayName = ProfileUtils.buildUserName(
            firstName = participant.user?.firstName,
            lastName = participant.user?.lastName,
        )
        binding.name.text = displayName


        Glide.with(context).load(participant.user?.avatar)
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.photo)

        resolveAddFriendButton(holder, position)


    }

    private fun resolveAddFriendButton(
        holder: ParticipantHolder,
        position: Int
    ) {

        holder.binding.btnAdd.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {

                holder.binding.btnAdd.isEnabled = false
                holder.binding.btnAdd.setImageResource(R.drawable.ic_success_circle_green)

                val participant = participants[position]

                val result = onAddFriendClick?.invoke(participant, position)

                if (result == false) {
                    holder.binding.btnAdd.isEnabled = true
                    holder.binding.btnAdd.setImageResource(R.drawable.add_friend)
                }


            }
        }

    }

    override fun getItemCount() = participants.size


}