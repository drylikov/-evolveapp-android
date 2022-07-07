package org.evolveapp.marathoner.utils

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.delay
import org.evolveapp.data.local.PrefDao
import org.evolveapp.marathoner.R
import java.util.*


fun Fragment.simulateLoadingData(
    shimmerLayout: ShimmerFrameLayout?,
    content: View?,
    loadingDuration: Long = 1_000,
    onComplete: () -> Unit = {},
) {

    (requireActivity() as AppCompatActivity).simulateLoadingData(
        shimmerLayout,
        content,
        loadingDuration,
        onComplete
    )

}


fun AppCompatActivity.simulateLoadingData(
    shimmerLayout: ShimmerFrameLayout?,
    content: View?,
    loadingDuration: Long = 1_000,
    onComplete: () -> Unit = {},
) {

    lifecycleScope.launchWhenCreated {

        content?.hide()
        shimmerLayout?.apply {
            show()
            showShimmer(true)
        }

        delay(loadingDuration)

        shimmerLayout?.apply {
            showShimmer(false)
            hide()
        }

        content?.show()
        onComplete()

    }
}


val Context.prefDao: PrefDao
    get() = PrefDao.getInstance(this)

val Activity.prefDao: PrefDao
    get() = application.prefDao

val Fragment.prefDao: PrefDao
    get() = requireContext().prefDao

val AndroidViewModel.prefDao: PrefDao
    get() = PrefDao.getInstance(getApplication())

val Locale.flagEmoji: String
    get() {
        val firstLetter = Character.codePointAt(country, 0) - 0x41 + 0x1F1E6
        val secondLetter = Character.codePointAt(country, 1) - 0x41 + 0x1F1E6
        return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
    }


fun Fragment.navigate(
    @IdRes
    resId: Int
) {

    val navOptions = NavOptions.Builder().apply {
        setEnterAnim(R.anim.enter_anim)
        setExitAnim(R.anim.exit_anim)
        setPopEnterAnim(R.anim.pop_enter_anim)
        setPopExitAnim(R.anim.pop_exit_anim)
    }.build()

    findNavController().navigate(resId, null, navOptions)

}