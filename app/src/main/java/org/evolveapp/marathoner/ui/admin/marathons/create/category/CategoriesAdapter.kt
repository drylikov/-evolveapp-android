package org.evolveapp.marathoner.ui.admin.marathons.create.category

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.evolveapp.domain.models.categories.Category
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.ListitemCreateMarathonCategoryBinding
import org.evolveapp.marathoner.utils.OnClick

class CategoriesAdapter(
    private val categories: List<Category>,
    var onCategorySelect: OnClick<Category>? = null,
    private val selectedCategory: Category? = null
) : RecyclerView.Adapter<CategoriesAdapter.Companion.CategoryHolder>() {

    private var selected: Category? = null

    init {
        selected = selectedCategory
    }

    companion object {
        class CategoryHolder(val binding: ListitemCreateMarathonCategoryBinding) :
            RecyclerView.ViewHolder(binding.root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val binding = DataBindingUtil.inflate<ListitemCreateMarathonCategoryBinding>(
            LayoutInflater.from(parent.context),
            R.layout.listitem_create_marathon_category,
            parent,
            false
        )
        return CategoryHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val category = categories[position]
        val binding = holder.binding
        val context = holder.itemView.context

        binding.name.text = category.name

        if (category == selected) {
            binding.cardParent.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimary
                )
            )
            binding.name.setTextColor(Color.WHITE)
        } else {
            binding.cardParent.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.cardCatNormal
                )
            )
            binding.name.setTextColor(Color.BLACK)
        }

        binding.cardParent.setOnClickListener {
            selected = category
            notifyDataSetChanged()
            onCategorySelect?.invoke(category, position)
        }

    }

    override fun getItemCount() = categories.size

}