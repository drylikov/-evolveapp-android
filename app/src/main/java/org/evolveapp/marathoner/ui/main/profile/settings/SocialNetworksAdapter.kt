package org.evolveapp.marathoner.ui.main.profile.settings

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.ListitemSocialNetworkBinding
import org.evolveapp.marathoner.ui.main.profile.settings.model.SocialNetwork
import org.evolveapp.marathoner.utils.OnClick

class SocialNetworksAdapter(
    private val socialNetworks: List<SocialNetwork>,
    var onClick: OnClick<SocialNetwork>? = null
) : RecyclerView.Adapter<SocialNetworksAdapter.Companion.SocialNetworkHolder>() {


    companion object {

        class SocialNetworkHolder(val binding: ListitemSocialNetworkBinding) :
            RecyclerView.ViewHolder(binding.root)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialNetworkHolder {
        val binding = DataBindingUtil.inflate<ListitemSocialNetworkBinding>(
            LayoutInflater.from(parent.context),
            R.layout.listitem_social_network,
            parent,
            false
        )
        return SocialNetworkHolder(binding)
    }

    override fun onBindViewHolder(holder: SocialNetworkHolder, position: Int) {
        val context = holder.itemView.context
        val binding = holder.binding
        val socialNetwork = socialNetworks[position]

        binding.cardParent.setOnClickListener { onClick?.invoke(socialNetwork, position) }

        binding.name.setText(socialNetwork.name)

        val colorTintActive = Color.WHITE
        val colorTintInactive = ContextCompat.getColor(context, R.color.color_social_tint_inactive)


        val colorCardActive = ContextCompat.getColor(context, R.color.colorPrimary)
        val colorCardInactive = ContextCompat.getColor(context, R.color.color_social_card_inactive)

        if (socialNetwork.isConnected) {

            binding.action.setImageResource(R.drawable.ic_round_arrow_forward_ios_24)

            binding.logo.setImageResource(socialNetwork.logo)
            binding.logo.imageTintList = ColorStateList.valueOf(colorTintActive)

            binding.cardLogo.setCardBackgroundColor(colorCardActive)

        } else {

            binding.action.setImageResource(R.drawable.ic_add)

            binding.logo.setImageResource(socialNetwork.logo)
            binding.logo.imageTintList = ColorStateList.valueOf(colorTintInactive)

            binding.cardLogo.setCardBackgroundColor(colorCardInactive)

        }

    }

    override fun getItemCount() = socialNetworks.size

}