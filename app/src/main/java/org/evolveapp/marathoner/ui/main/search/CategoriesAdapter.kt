package org.evolveapp.marathoner.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.ListitemCategoryBinding

class CategoriesAdapter(
    private val categories: List<Category>
) : RecyclerView.Adapter<CategoriesAdapter.Companion.CategoryHolder>() {

    companion object {

        class CategoryHolder(val binding: ListitemCategoryBinding) :
            RecyclerView.ViewHolder(binding.root)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val binding: ListitemCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.listitem_category,
            parent,
            false
        )

        return CategoryHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val binding = holder.binding
        val category = categories[position]

        binding.name.text = category.name

    }

    override fun getItemCount() = categories.size

}