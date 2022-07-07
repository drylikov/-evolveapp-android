package org.evolveapp.marathoner.ui.admin.marathon.tasks.manage

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.ListitemTaskTabBinding
import org.evolveapp.marathoner.utils.OnClick

class TabsAdapter(
    private val tabs: List<Tab>,
    var onTabClick: OnClick<Tab>? = null
) : RecyclerView.Adapter<TabsAdapter.Companion.TabHolder>() {

    private var selected: Int = 0

    companion object {

        class TabHolder(val binding: ListitemTaskTabBinding) : RecyclerView.ViewHolder(binding.root)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabHolder {
        val binding: ListitemTaskTabBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.listitem_task_tab,
            parent,
            false
        )

        return TabHolder(binding)
    }

    override fun onBindViewHolder(holder: TabHolder, position: Int) {
        val tab = tabs[position]
        val binding = holder.binding
        val context = holder.itemView.context



        binding.name.text = tab.name

        if (tab.notifications) {
            val notificationIcon = ContextCompat.getDrawable(context, R.drawable.ic_dot)
            binding.name.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                notificationIcon,
                null
            )
        } else {
            binding.name.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
        }

        if (position == selected) {

            val cardColor = ContextCompat.getColor(context, R.color.color_0D000000)

            binding.cardParent.setCardBackgroundColor(cardColor)

            binding.name.setTextColor(Color.BLACK)
        } else {

            binding.cardParent.setCardBackgroundColor(Color.TRANSPARENT)

            val textColor = ContextCompat.getColor(context, R.color.color_00000080)

            binding.name.setTextColor(textColor)

        }


        binding.cardParent.setOnClickListener {
            if (selected != position) {
                onTabClick?.invoke(tab, position)
                selected = position
                notifyDataSetChanged()
            } else {
                // TODO: 9/4/2021
            }
        }

    }

    override fun getItemCount() = tabs.size

}