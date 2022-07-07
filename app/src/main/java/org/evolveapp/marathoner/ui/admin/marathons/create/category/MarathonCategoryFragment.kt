package org.evolveapp.marathoner.ui.admin.marathons.create.category

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import org.evolveapp.domain.models.categories.Category
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.FragmentMarathonCategoryBinding
import org.evolveapp.marathoner.ui.admin.marathons.create.CreateMarathonActivity
import org.evolveapp.marathoner.utils.*

private const val ARG_CAT_JSON = "cat_json"

@AndroidEntryPoint
class MarathonCategoryFragment : BottomSheetDialogFragment() {

    private var initCat: Category? = null

    private val viewModel by viewModels<MarathonCategoryViewModel>()

    private var binding: FragmentMarathonCategoryBinding? = null

    private var categoriesAdapter: CategoriesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            initCat = Gson().fromJson(it.getString(ARG_CAT_JSON), Category::class.java)
        }

        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_marathon_category, container, false)

        return binding?.root
    }

    private fun handleSelectButton() {
        launchViewLifecycleScope {
            viewModel.category.collect { cat ->
                val isEnabled = cat != null
                binding?.select?.isEnabled = isEnabled
            }
        }

        binding?.select?.setOnClickListener {

            (requireActivity() as CreateMarathonActivity).onCategorySelect(viewModel.category.value!!)
            dismiss()

        }

    }

    private fun initCategoriesUI(categories: List<Category>) {

        categoriesAdapter = CategoriesAdapter(
            categories,
            onCategorySelected,
            initCat
        )

        val lm = getGridLayoutManager()

        val spanCount = 3 // 3 columns

        val spacingInPixels = convertDpToPx(requireContext(), 16f)

        val includeEdge = true


        binding?.categories?.apply {
            layoutManager = lm
            addItemDecoration(
                GridSpacingItemDecoration(
                    spanCount,
                    spacingInPixels.toInt(),
                    includeEdge
                )
            )

            adapter = categoriesAdapter
        }

    }

    private val onCategorySelected = OnClick<Category> { category, _ ->
        viewModel.setCategory(category)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleSelectButton()
        fetchCategories()

        initCat?.apply { viewModel.setCategory(this) }


        binding?.close?.setOnClickListener {
            dismiss()
        }

    }

    private fun fetchCategories() {
        launchViewLifecycleScope {

            binding?.loading?.loadingLayout?.show()

            val request = viewModel.getCategories().first()

            request.onSuccess { categories ->
                initCategoriesUI(categories)
                binding?.loading?.loadingLayout?.gone()
            }

            request.onFailure { _, _ ->
                showToast(R.string.something_went_wrong_try_again_later)
                dismiss()
            }

        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding?.categories?.adapter = null
        binding = null
    }

    companion object {


        interface OnCategorySelect {

            fun onCategorySelect(category: Category)

        }

        @JvmStatic
        fun newInstance(init: Category? = null) = MarathonCategoryFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_CAT_JSON, Gson().toJson(init))
            }
        }
    }


    private fun getGridLayoutManager(): RecyclerView.LayoutManager {

        val lm = GridLayoutManager(requireContext(), 3)

        return lm
    }

}