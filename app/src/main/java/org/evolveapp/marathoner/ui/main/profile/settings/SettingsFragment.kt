package org.evolveapp.marathoner.ui.main.profile.settings

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.auth.FirebaseAuth
import com.mukesh.countrypicker.CountryPicker
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.FragmentSettingsBinding
import org.evolveapp.marathoner.services.UploadProfilePicService
import org.evolveapp.marathoner.ui.main.profile.ProfileUtils
import org.evolveapp.marathoner.ui.main.profile.settings.model.SocialNetwork
import org.evolveapp.marathoner.ui.splash.SplashActivity
import org.evolveapp.marathoner.utils.*
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel by viewModels<SettingsViewModel>()

    private var binding: FragmentSettingsBinding? = null

    private var socialNetworksAdapter: SocialNetworksAdapter? = null

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.apply {
                val path = PathUtil.getPath(requireContext(), this)
                viewModel.selectedAvatar.value = path
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


    private val profilePicBroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            Timber.d("onReceive()")

            val isSuccess: Boolean =
                intent!!.getBooleanExtra(UploadProfilePicService.IS_SUCCESS, false)

            if (isSuccess) {

                val photoUrl: String? = intent.getStringExtra(UploadProfilePicService.PHOTO_URL)

                prefDao.userPhoto = photoUrl

                showToast(R.string.msg_avatar_updated, true)

            } else {

                viewModel.selectedAvatar.value = null

                showToast(R.string.msg_unable_to_update_avatar, true)
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel


        return binding?.root
    }

    override fun onResume() {
        super.onResume()

        val filter = IntentFilter(UploadProfilePicService.ACTION_RECEIVE_RESPONSE)
        requireContext().registerReceiver(profilePicBroadcastReceiver, filter)

    }

    private fun initSocialNetworksUI(socialNetworks: List<SocialNetwork>) {

        socialNetworksAdapter = SocialNetworksAdapter(socialNetworks)

        binding?.socialNetworks?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = socialNetworksAdapter
        }

    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(profilePicBroadcastReceiver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.close?.setOnClickListener { findNavController().navigateUp() }

        handleCountryUI()
        collectUiMessages()
        handleLogoutButton()
        handleBirthdayUI()
        handleLogoutButton()
        handleSaveButton()
        handleAddPhoto()

        val resolvedAvatar =
            viewModel.selectedAvatar.value.takeUnless { it.isNullOrBlank() } ?: prefDao.userPhoto
        updateAvatarUI(resolvedAvatar)

        initSocialNetworksUI(socialNetworks = ProfileUtils.getAvailableSocialNetworks(requireActivity()))

    }

    private fun handleAddPhoto() {
        binding?.addPhoto?.setOnClickListener { pickImageFromGallery() }

        launchViewLifecycleScope {
            viewModel.selectedAvatar.collect { photo ->

                when (photo) {
                    null -> {
                        updateAvatarUI(prefDao.userPhoto)
                    }

                    else -> {
                        updateAvatarUI(photo)

                        val serviceIntent =
                            Intent(requireContext(), UploadProfilePicService::class.java)
                        serviceIntent.putExtra(UploadProfilePicService.EXTRA_IMAGE_PATH, photo)

                        ContextCompat.startForegroundService(requireContext(), serviceIntent)
                    }
                }


            }
        }

    }

    private fun updateAvatarUI(photo: String?) {
        Glide.with(requireContext()).load(photo)
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.ic_profile_userpic)
            .error(R.drawable.ic_profile_userpic)
            .into(binding!!.avatar)
    }

    private fun handleSaveButton() {
        binding?.save?.setOnClickListener { onSaveClick() }
    }

    private fun onSaveClick() {

        if (viewModel.validateInputsOrShowError()) {
            launchViewLifecycleScope {

                (requireActivity() as AppCompatActivity).hideSoftKeyboard()
                binding?.username?.clearFocus()
                binding?.fName?.clearFocus()
                binding?.lName?.clearFocus()

                binding?.loading?.apply {
                    tvLoadingMessage.text = getString(R.string.updating_profile)
                    root.show()
                }

                val response =
                    viewModel.updateProfile(params = viewModel.buildUpdateProfileRequestParams())
                        .first()

                response.onSuccess { profile ->
                    showToast(R.string.profile_updated)
                    viewModel.refreshLocalProfileData(profile)
                    findNavController().navigateUp()
                }

                response.onFailure { message, code ->
                    showToast(R.string.something_went_wrong_try_again_later)
                    binding?.loading?.root?.gone()
                }

            }
        }

    }

    private fun pickImageFromGallery() {
        requestPermissionsContract.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
    }

    private fun handleCountryUI() {
        launchViewLifecycleScope {
            viewModel.country.collect { countryCode ->
                Timber.d(countryCode)

                val displayCountryWithLogo =
                    ProfileUtils.getDisplayCountryWithLogo(countryCode ?: "")
                binding?.country?.setText(displayCountryWithLogo)

            }
        }

        binding?.country?.setOnClickListener {

            val builder = CountryPicker.Builder().with(requireContext()).listener {
                viewModel.country.value = it.code
            }

            val picker = builder.build()
            picker.showBottomSheet(requireActivity() as AppCompatActivity)
        }

    }

    private fun handleBirthdayUI() {

        launchViewLifecycleScope {
            viewModel.dateOfBirth.collect {

                it?.apply {

                    val formatted = DateUtils.format(
                        date = this,
                        format = DateUtils.PATTERN_2020_08_31
                    )

                    binding?.birthday?.setText(formatted)
                }
            }
        }


        binding?.birthday?.setOnClickListener {

            val c = if (viewModel.dateOfBirth.value != null) {

                Calendar.getInstance().apply {
                    time = viewModel.dateOfBirth.value!!
                }

            } else {
                Calendar.getInstance()
            }

            showDatePicker(initialSelection = c)

        }

    }

    private fun handleLogoutButton() {

        binding?.logout?.setOnClickListener {
            showLogoutDialog()
        }

    }

    private fun performLogout() {
        launchViewLifecycleScope {

            binding?.loading?.loadingLayout?.show()
            FirebaseAuth.getInstance().signOut()

            viewModel.logout(prefDao.refreshToken ?: "").first().onSuccess {
                prefDao.clearAll()
                goTo(SplashActivity::class.java)
                requireActivity().finishAffinity()
            }.onFailure { message, code ->
                showToast(R.string.something_went_wrong_try_again_later)
                prefDao.clearAll()
                goTo(SplashActivity::class.java)
                requireActivity().finishAffinity()
            }

        }
    }

    private fun showLogoutDialog() {

        showMaterialDialog {

            setTitle(R.string.confirm_logout)
            setMessage(R.string.msg_confirm_logout)

            setIcon(R.drawable.ic_round_logout_24)

            onPositiveButtonClick(R.string.log_out) {
                performLogout()
            }

            onNegativeButtonClick(R.string.cancel) { dismiss() }

        }

    }

    private fun collectUiMessages() {
        launchViewLifecycleScope {
            viewModel.uiMessages.collect { resId -> resId?.apply { showToast(this) } }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.socialNetworks?.adapter = null
        binding = null
    }

    private fun showDatePicker(initialSelection: Calendar = Calendar.getInstance()) {

        val datePickerDialog = DatePickerDialog.newInstance(onBirthdaySet, initialSelection)

        datePickerDialog.maxDate = Calendar.getInstance()

        datePickerDialog.show(childFragmentManager, "")

    }

    private val onBirthdaySet =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            viewModel.setBirthday(year, monthOfYear, dayOfMonth)
        }

}