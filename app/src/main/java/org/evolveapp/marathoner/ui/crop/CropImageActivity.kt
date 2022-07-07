package org.evolveapp.marathoner.ui.crop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.isseiaoki.simplecropview.callback.CropCallback
import com.isseiaoki.simplecropview.callback.LoadCallback
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.ActivityCropImageBinding
import org.evolveapp.marathoner.utils.RuntimeLocaleChanger
import org.evolveapp.marathoner.utils.showToast
import timber.log.Timber
import java.io.ByteArrayOutputStream

// FIXME: 10/14/2021 large images sizes cause crash

const val EXTRA_CROP_REQUEST = "crop_request"
const val EXTRA_IMG_RESULT = "img_result"

class CropImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCropImageBinding

    private val cropRequest: CropRequest by lazy { intent.getParcelableExtra(EXTRA_CROP_REQUEST)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_crop_image)

        binding.back.setOnClickListener { onBackPressed() }
        binding.add.setOnClickListener { performCrop() }

        binding.cropImageView.setInitialFrameScale(0.75f)
        binding.cropImageView.setCropMode(cropRequest.cropMode)
        binding.cropImageView.load(cropRequest.imageUri).execute(loadCallback)

    }

    private val cropCallback = object : CropCallback {
        override fun onError(e: Throwable?) {
            Timber.e(e)
            showToast(R.string.something_went_wrong_try_again_later)
        }

        override fun onSuccess(cropped: Bitmap) {
            sendResults(cropped)
        }
    }

    private fun sendResults(bitmap: Bitmap) {

        //Convert to byte array
        val stream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.JPEG, 50, stream)
        val byteArray = stream.toByteArray()

        Timber.d("Size: ${byteArray.size / 1024}KB")

        val result = Intent()
        result.putExtra(EXTRA_IMG_RESULT, byteArray)
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    private fun performCrop() {
        binding.cropImageView.crop(cropRequest.imageUri).execute(cropCallback)
    }

    private val loadCallback = object : LoadCallback {
        override fun onError(e: Throwable?) {
            Timber.e(e)
        }

        override fun onSuccess() {
            Timber.d("onSuccess")
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(RuntimeLocaleChanger.wrapContext(base))
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.pop_enter_anim, R.anim.pop_exit_anim)
    }

}