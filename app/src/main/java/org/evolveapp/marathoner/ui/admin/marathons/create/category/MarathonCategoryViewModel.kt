package org.evolveapp.marathoner.ui.admin.marathons.create.category

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.evolveapp.domain.models.categories.Category
import org.evolveapp.domain.usecases.marathon.GetCategoriesUseCase
import javax.inject.Inject

@HiltViewModel
class MarathonCategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
) : ViewModel() {

    private val _category = MutableStateFlow<Category?>(null)
    val category: StateFlow<Category?> = _category

    fun setCategory(category: Category) {
        _category.value = category
    }

    fun getCategories() = getCategoriesUseCase.launch()

}