package org.evolveapp.marathoner.ui.admin.marathon.posts.add

import android.Manifest
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.ActivityAddPostBinding
import org.evolveapp.marathoner.ui.admin.marathon.posts.schedule.ScheduleFragment
import org.evolveapp.marathoner.utils.*
import java.io.File
import java.util.*


@AndroidEntryPoint
class AddPostActivity : AppCompatActivity(), ScheduleFragment.Companion.OnTimeSelectListener {

    private val viewModel by viewModels<AddPostViewModel>()

    private lateinit var binding: ActivityAddPostBinding

    private val marathonId: String by lazy { intent.getStringExtra(EXTRA_MARATHON_ID) ?: "" }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.apply {

                when (viewModel.pickedContentType) {

                    MediaUtils.TYPE_IMAGE -> {
                        resolveSelectedPhoto(this)
                    }
                    MediaUtils.TYPE_VIDEO -> {
                        resolveSelectedVideo(this)
                    }
                    else -> {
                        // TODO: 10/9/2021
                    }
                }

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
                getContent.launch(viewModel.pickedContentType)
            } else {
                showToast(R.string.msg_grant_storage_permission_from_settings)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_post)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        handleCloseButton()
        handlePublishButton()
        handleUploadPhotoButton()
        handleUploadVideoButton()
        handleScheduleButton()
        handleScheduledTime()

        collectPickContentRequests()
        collectUploadedImage()
        collectUploadedVideo()

    }

    private fun handleScheduledTime() {
        launchLifecycleScope {
            viewModel.scheduledTime.collect { date: Date? ->

                if (date == null) {
                    binding.scheduleLabel.setText(R.string.now)
                } else {
                    binding.scheduleLabel.setText(R.string.late)
                }

            }
        }
    }

    private fun handleScheduleButton() {

        binding.schedule.setOnClickListener {

            ScheduleFragment.newInstance(
                timeMillis = viewModel.scheduledTime.value?.time ?: -1L
            ).show(supportFragmentManager, "")

        }

    }

    private fun collectUploadedVideo() {
        launchLifecycleScope {
            viewModel.videoPath.collect { path ->
                handleUploadedVideoUI(path)
            }
        }
    }

    private fun collectPickContentRequests() {
        launchLifecycleScope {
            viewModel.requestPickContent.collect { contentType ->

                if (!contentType.isNullOrBlank()) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        getContent.launch(contentType)
                    } else {
                        requestPermissionsContract.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                    }
                }

            }
        }
    }

    private fun handleUploadVideoButton() {

        binding.uploadVideo.setOnClickListener {
            viewModel.requestPickContent(MediaUtils.TYPE_VIDEO)
        }
    }

    private fun handleCloseButton() = binding.close.setOnClickListener { onBackPressed() }

    private fun handleUploadPhotoButton() {

        binding.uploadPhoto.setOnClickListener {
            viewModel.requestPickContent(MediaUtils.TYPE_IMAGE)
        }

    }

    private fun handlePublishButton() {

        launchLifecycleScope {
            viewModel.postText.collect {
                binding.publish.isEnabled = it.isNotBlank()
            }
        }

        binding.publish.setOnClickListener {
            publishPost()
        }

    }

    private fun publishPost() {
        launchLifecycleScope {

            hideSoftKeyboard()

            binding.loading.apply {
                tvLoadingMessage.setText(R.string.publishing_your_post)
                loadingLayout.show()
            }


            val compressedImage: File? = if (viewModel.imagePath.value.isNullOrBlank()) {
                null
            } else {
                compressImage(
                    context = this@AddPostActivity,
                    imagePath = viewModel.imagePath.value ?: ""
                )
            }

            val response = viewModel.createPost(
                marathonId = marathonId,
                text = viewModel.postText.value,
                activateDatetime = DateUtils.format(
                    date = viewModel.scheduledTime.value.takeIf { it != null } ?: Date(),
                    format = "yyyy-MM-dd HH:mm:ss.SSS"
                ),
                imageFile = compressedImage,
                videoFile = null
            ).first()

            response.onSuccess {
                showToast(R.string.msg_post_published)
                finish()
            }

            response.onFailure { message, code ->
                showToast(R.string.something_went_wrong_try_again_later)
                binding.loading.loadingLayout.gone()
            }

        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(RuntimeLocaleChanger.wrapContext(base))
    }

    private fun resolveSelectedPhoto(uri: Uri) {
        val path = PathUtil.getPath(this, uri)
        viewModel.imagePath.value = path
    }

    private fun resolveSelectedVideo(uri: Uri) {
        val path = PathUtil.getPath(this, uri)
        viewModel.videoPath.value = path
    }

    private fun collectUploadedImage() {
        launchLifecycleScope {
            viewModel.imagePath.collect { path ->

                handleUploadedPhotoUI(path)

            }
        }
    }

    private fun handleUploadedPhotoUI(path: String?) {

        when {

            path.isNullOrBlank() -> {

                binding.layoutPhoto.gone()

            }

            else -> {

                binding.layoutPhoto.show()

                Glide.with(this).load(path)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(binding.photo)

            }

        }

    }

    private fun handleUploadedVideoUI(path: String?) {

        when {

            path.isNullOrBlank() -> {

                binding.layoutVideo.gone()

            }

            else -> {

                binding.layoutVideo.show()

                Glide.with(this).load(path)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(binding.video)

            }


        }

    }

    companion object {

        private const val EXTRA_MARATHON_ID = "marathon_id"

        fun launch(activity: Activity, marathonId: String) {
            activity.goTo(AddPostActivity::class.java) {
                putExtra(EXTRA_MARATHON_ID, marathonId)
            }
        }

    }

    override fun onNowSelected() {
        viewModel.scheduledTime.value = null
    }

    override fun onLateSelected(date: Date) {

        viewModel.scheduledTime.value = date
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.pop_enter_anim, R.anim.pop_exit_anim)
    }

}