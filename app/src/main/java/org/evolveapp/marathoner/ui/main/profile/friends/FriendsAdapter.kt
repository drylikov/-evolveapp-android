package org.evolveapp.marathoner.ui.main.profile.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import org.evolveapp.domain.models.friends.Friend
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.ListitemUserFriendBinding
import org.evolveapp.marathoner.ui.main.profile.ProfileUtils

class FriendsAdapter(
    private val friends: List<Friend>
) : RecyclerView.Adapter<FriendsAdapter.Companion.FriendHolder>() {

    companion object {

        class FriendHolder(
            val binding: ListitemUserFriendBinding
        ) : RecyclerView.ViewHolder(binding.root)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendHolder {

        val binding: ListitemUserFriendBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.listitem_user_friend,
            parent,
            false
        )

        return FriendHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendHolder, position: Int) {
        val context = holder.itemView.context
        val binding = holder.binding
        val friend = friends[position]

        binding.name.text = ProfileUtils.buildUserName(
            firstName = friend.firstName,
            lastName = friend.lastName
        )

        Glide.with(context).load(friend.avatar?.localUrl)
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.ic_profile_userpic)
            .error(R.drawable.ic_profile_userpic)
            .into(binding.photo)


    }

    override fun getItemCount() = friends.size

}