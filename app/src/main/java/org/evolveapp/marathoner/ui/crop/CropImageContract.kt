package org.evolveapp.marathoner.ui.crop

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class CropImageContract : ActivityResultContract<CropRequest, ByteArray?>() {

    override fun createIntent(context: Context, input: CropRequest?): Intent {
        return Intent(context, CropImageActivity::class.java).apply {
            putExtra(EXTRA_CROP_REQUEST, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): ByteArray? {
        return when (resultCode) {

            Activity.RESULT_OK -> {

                val img = intent?.getByteArrayExtra(EXTRA_IMG_RESULT)
                img
            }

            else -> {
                null
            }

        }
    }


}