package org.evolveapp.marathoner.utils

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.evolveapp.marathoner.R
import java.util.*

// This is general utils (not depending on the project) and can easily implement in other projects


fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Activity.goTo(cls: Class<*>, builder: Intent.() -> Unit = {}) {

    val targetIntent = Intent(this, cls).apply(builder)

    val options = ActivityOptions.makeSceneTransitionAnimation(this)
    startActivity(targetIntent/*, options.toBundle()*/)
    overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim)

}

fun Fragment.goTo(cls: Class<*>, builder: Intent.() -> Unit = {}) =
    requireActivity().goTo(cls, builder)


fun Context.showToast(message: String, lengthLong: Boolean = false) {
    val length = if (lengthLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    Toast.makeText(this, message, length).show()
}

fun Context.showToast(@StringRes resId: Int, lengthLong: Boolean = false) {
    val length = if (lengthLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    Toast.makeText(this, resId, length).show()
}

fun Activity.showToast(message: String, lengthLong: Boolean = false) {
    application.showToast(message, lengthLong)
}

fun Activity.showToast(@StringRes resId: Int, lengthLong: Boolean = false) {
    application.showToast(resId, lengthLong)
}

fun Fragment.showToast(message: String, lengthLong: Boolean = false) {
    requireContext().showToast(message, lengthLong)
}

fun Fragment.showToast(@StringRes resId: Int, lengthLong: Boolean = false) {
    requireContext().showToast(resId, lengthLong)
}


fun String?.isValidEmail(): Boolean {
    return (!TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this.toString()).matches())
}

fun AppCompatActivity.hideSoftKeyboard() {

    currentFocus?.apply {

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)

    }
}


fun Context.vibrate(milliseconds: Long) {

    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                milliseconds,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    } else {

        @Suppress("DEPRECATION")
        vibrator.vibrate(milliseconds)

    }

}

/**
 * Return string based on current app local configuration
 */
fun Context.localize(enText: String, arText: String): String {

    return when {

        Locale.getDefault().equals(Locale("ar")) -> arText

        Locale.getDefault().equals(Locale("en")) -> enText

        else -> enText

    }

}

/**
 * Return string based on current app local configuration
 */
fun Fragment.localize(enText: String, arText: String) = requireContext().localize(enText, arText)

/**
 * Return string based on current app local configuration
 */
fun AppCompatActivity.localize(enText: String, arText: String) =
    applicationContext.localize(enText, arText)

/**
 * This method converts device specific pixels to density independent pixels.
 *
 * @param px      A value in px (pixels) unit. Which we need to convert into db
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent dp equivalent to px value
 */
fun convertPxToDp(context: Context, px: Float): Float {
    return px / context.resources.displayMetrics.density
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

/**
 * This method converts dp unit to equivalent pixels, depending on device density.
 *
 * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent px equivalent to dp depending on device density
 */
fun convertDpToPx(context: Context, dp: Float): Float {
    return dp * context.resources.displayMetrics.density
}


