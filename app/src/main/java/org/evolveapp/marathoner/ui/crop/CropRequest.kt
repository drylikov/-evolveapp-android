package org.evolveapp.marathoner.ui.crop

import android.net.Uri
import android.os.Parcelable
import com.isseiaoki.simplecropview.CropImageView
import kotlinx.parcelize.Parcelize

@Parcelize
data class CropRequest(
    val imageUri: Uri,
    val cropMode: CropImageView.CropMode
): Parcelable