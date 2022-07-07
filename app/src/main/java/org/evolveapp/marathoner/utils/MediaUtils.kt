package org.evolveapp.marathoner.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import id.zelory.compressor.Compressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.ByteArrayOutputStream
import java.io.File

object MediaUtils {

    const val TYPE_IMAGE = "image/*"
    const val TYPE_VIDEO = "video/*"

}


fun ByteArray.toBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, size)

fun Bitmap.toFile(): File {
    // FIXME: 10/16/2021
    return File("")
}

fun Bitmap.toUri(
    context: Context,
    title: String = "marathoner_image_${System.currentTimeMillis()}",
    quality: Int = 25
): Uri? {

    // TODO: 10/17/2021 Create app folder or save image in memory

    val bytes = ByteArrayOutputStream()

    compress(Bitmap.CompressFormat.JPEG, quality, bytes)
    val path = MediaStore.Images.Media.insertImage(
        context.contentResolver,
        this,
        title,
        null
    )

    return Uri.parse(path)

}

@Throws(Exception::class)
suspend fun compressImage(
    context: Context,
    imagePath: String
): File {
    val originalImage = File(imagePath)
    val compressed =
        CoroutineScope(Dispatchers.IO).async { Compressor.compress(context, originalImage) }
    return compressed.await()
}