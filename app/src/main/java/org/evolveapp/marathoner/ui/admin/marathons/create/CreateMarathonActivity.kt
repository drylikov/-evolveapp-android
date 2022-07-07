package org.evolveapp.marathoner.ui.admin.marathons.create

import android.Manifest
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.isseiaoki.simplecropview.CropImageView
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import org.evolveapp.domain.models.categories.Category
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.ActivityCreateMarathonBinding
import org.evolveapp.marathoner.ui.admin.marathons.create.category.MarathonCategoryFragment
import org.evolveapp.marathoner.ui.crop.CropImageContract
import org.evolveapp.marathoner.ui.crop.CropRequest
import org.evolveapp.marathoner.utils.*
import java.util.*


@AndroidEntryPoint
class CreateMarathonActivity : AppCompatActivity(),
    MarathonCategoryFragment.Companion.OnCategorySelect {

    private lateinit var binding: ActivityCreateMarathonBinding

    private val viewModel by viewModels<CreateMarathonViewModel>()

    private val datePickerDialog by lazy { DatePickerDialog.newInstance(dateSetListener) }

    private val cropImageContract = registerForActivityResult(CropImageContract()) { result ->
        result?.apply { viewModel.image.value = this }
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.apply {
                val cropRequest = CropRequest(
                    imageUri = this,
                    cropMode = CropImageView.CropMode.RATIO_16_9
                )
                cropImageContract.launch(cropRequest)
            }
        }

    private val requestPermissionsContract =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

            var isAllPermissionsGranted = true

            permissions.entries.forEach {
                if (!it.value) {
                    isAllPermissionsGranted = false
                    return@forEach
                }
            }

            if (isAllPermissionsGranted) {
                getContent.launch(MediaUtils.TYPE_IMAGE)
            } else {
                showToast(R.string.msg_grant_storage_permission_from_settings)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_marathon)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.category.setOnClickListener {
            MarathonCategoryFragment.newInstance(
                init = viewModel.category.value
            ).show(supportFragmentManager, null)
        }

        binding.close.setOnClickListener { onBackPressed() }
        binding.create.setOnClickListener { onCreateMarathonClick() }

        handleImageUI()
        handleStartDateUI()
        handleEndDateUI()
        handleSelectedCategoryUI()

        binding.layoutAddPic.setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                getContent.launch(MediaUtils.TYPE_IMAGE)
            } else {
                requestPermissionsContract.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            }

        }

        binding.layoutStartDate.setOnClickListener {
            showDatePickerDialog(
                tag = TAG_PICK_START_DATE,
                initialSelection = viewModel.startDate.value ?: Calendar.getInstance()
            )
        }

        binding.layoutEndDate.setOnClickListener {

            if (viewModel.startDate.value == null) {
                showToast(R.string.please_pick_start_date_first)
                return@setOnClickListener
            }

            showDatePickerDialog(
                tag = TAG_PICK_END_DATE,
                initialSelection = viewModel.endDate.value ?: Calendar.getInstance()
            )
        }


        /*binding.registrationBefore.setOnClickListener {

            DatePickerDialog().apply {
                accentColor = ContextCompat.getColor(
                    this@CreateMarathonActivity,
                    R.color.colorPrimary
                )
            }.show(supportFragmentManager, null)

        }*/

    }

    private fun handleSelectedCategoryUI() {
        launchLifecycleScope {
            viewModel.category.collect { cat ->
                if (cat == null) {
                    binding.selectedCategory.text = ""
                } else {
                    binding.selectedCategory.text = cat.name
                }
            }
        }
    }

    private fun onCreateMarathonClick() {

        val error = viewModel.validateInputs()

        if (error != null) {
            showToast(error)
        } else {
            createMarathon()
        }

    }

    private fun createMarathon() {
        launchLifecycleScope {
            hideSoftKeyboard()
            binding.name.clearFocus()
            binding.desc.clearFocus()
            binding.loading.apply {
                tvLoadingMessage.setText(R.string.creating_marathon)
                loadingLayout.show()
            }


            val bitmap = viewModel.image.value!!.toBitmap()
            val uri = bitmap.toUri(this)
            val path = PathUtil.getPath(this, uri)
            val compressedImage = compressImage(this, path)

            val request = viewModel.createMarathon(
                name = viewModel.name.value,
                description = viewModel.description.value,
                startDate = viewModel.getStartDateFormattedOrNull() ?: "",
                endDate = viewModel.getEndDateFormattedOrNull() ?: "",
                category = viewModel.category.value?.id.toString(),
                isActive = true,
                isPublic = !viewModel.isPrivate.value,
                avatar = compressedImage,
            ).first()


            request.onSuccess {
                showToast(R.string.marathon_created)
                finish()
                // TODO: 10/16/2021 Go to created marathon rather than calling finish()
            }

            request.onFailure { _, _ ->
                binding.loading.loadingLayout.gone()
                showToast(R.string.something_went_wrong_try_again_later)
            }

        }
    }

    private fun handleImageUI() {

        launchLifecycleScope {
            viewModel.image.collect { byteArray ->
                when (byteArray) {
                    null -> {
                        val colorCoverOverlay = ContextCompat.getColor(this, R.color.color_EBEBED)
                        val colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary)

                        binding.layoutAddPic.setBackgroundColor(colorCoverOverlay)
                        binding.addPhoto.setTextColor(colorPrimary)
                        binding.iconAddPhoto.imageTintList = ColorStateList.valueOf(colorPrimary)
                    }
                    else -> {
                        val bitmap = byteArray.toBitmap()
                        Glide.with(this@CreateMarathonActivity).load(bitmap).into(binding.cover)

                        val colorCoverOverlay = ContextCompat.getColor(this, R.color.colorCoverOverlay)
                        binding.layoutAddPic.setBackgroundColor(colorCoverOverlay)

                        binding.addPhoto.setTextColor(Color.WHITE)
                        binding.iconAddPhoto.imageTintList = ColorStateList.valueOf(Color.WHITE)

                    }
                }
            }
        }

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(RuntimeLocaleChanger.wrapContext(base))
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.pop_enter_anim, R.anim.pop_exit_anim)
    }

    private fun showDatePickerDialog(
        tag: String,
        initialSelection: Calendar = Calendar.getInstance()
    ) {
        viewModel.tag = tag
        datePickerDialog.initialize(dateSetListener, initialSelection)

        when (tag) {
            TAG_PICK_START_DATE -> datePickerDialog.minDate = Calendar.getInstance()
            TAG_PICK_END_DATE -> datePickerDialog.minDate =
                viewModel.startDate.value ?: Calendar.getInstance()
        }

        datePickerDialog.show(supportFragmentManager, tag)
    }

    private fun handleStartDateUI() {
        launchLifecycleScope {
            viewModel.startDate.collect { calendar ->
                when (calendar) {
                    null -> binding.labelStartDate.setText(R.string.tap_to_select)
                    else -> {
                        val formatted = DateUtils.formatDateAsString(
                            year = calendar[Calendar.YEAR],
                            month = calendar[Calendar.MONTH] + 1,
                            day = calendar[Calendar.DAY_OF_MONTH]
                        )
                        binding.labelStartDate.text = formatted
                    }
                }
            }
        }
    }

    private fun handleEndDateUI() {
        launchLifecycleScope {
            viewModel.endDate.collect { calendar ->
                when (calendar) {
                    null -> {
                        binding.labelEndDate.setText(R.string.tap_to_select)
                    }
                    else -> {
                        val formatted = DateUtils.formatDateAsString(
                            year = calendar[Calendar.YEAR],
                            month = calendar[Calendar.MONTH] + 1,
                            day = calendar[Calendar.DAY_OF_MONTH]
                        )
                        binding.labelEndDate.text = formatted
                    }
                }
            }
        }
    }

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

            when (viewModel.tag) {
                TAG_PICK_START_DATE -> viewModel.setStartDate(year, monthOfYear, dayOfMonth)
                TAG_PICK_END_DATE -> viewModel.setEndDate(year, monthOfYear, dayOfMonth)
            }
        }

    override fun onCategorySelect(category: Category) {
        viewModel.setCategory(category)
    }

}