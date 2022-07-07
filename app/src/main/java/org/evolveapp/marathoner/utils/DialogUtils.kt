package org.evolveapp.marathoner.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Typeface
import android.text.Html
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.evolveapp.marathoner.R


class DialogAttributesBuilder(
    private val context: Context,
) {

    private var title: String? = null

    private var message: String? = null

    @DrawableRes
    private var icon: Int? = null

    var isCancelable: Boolean = true

    private var positiveButtonText: String? = null
    private var positiveButtonAction: (DialogInterface.() -> Unit)? = null

    private var negativeButtonText: String? = null
    private var negativeButtonAction: (DialogInterface.() -> Unit)? = null


    fun setTitle(@StringRes resId: Int) {
        title = context.getString(resId)
    }

    @JvmName("setTitleString")
    fun setTitle(text: String) {
        title = text
    }

    fun setIcon(@DrawableRes resId: Int) {
        icon = resId
    }

    fun setMessage(resId: Int) {
        message = context.getString(resId)
    }

    @JvmName("setMessageString")
    fun setMessage(text: String) {
        message = text
    }

    /*fun onPositiveButtonClick(
        text: String = context.getString(R.string.okay),
        action: (DialogInterface.() -> Unit)
    ) {
        positiveButtonText = text
        positiveButtonAction = action
    }*/

    fun onPositiveButtonClick(
        resId: Int = R.string.okay,
        action: (DialogInterface.() -> Unit)
    ) {
        positiveButtonText = context.getString(resId)
        positiveButtonAction = action
    }

    fun onNegativeButtonClick(resId: Int = R.string.cancel, action: (DialogInterface.() -> Unit)) {
        negativeButtonText = context.getString(resId)
        negativeButtonAction = action
    }

    /*fun onNegativeButtonClick(text: String? = null, action: (DialogInterface.() -> Unit)) {
        negativeButtonText = text
        negativeButtonAction = action
    }*/

    fun build() {

        val builder = MaterialAlertDialogBuilder(context).apply {

            title?.apply { setTitle(this) }
            message?.apply {
                val message = HtmlCompat.fromHtml("<b>$this</b>", HtmlCompat.FROM_HTML_MODE_COMPACT)
                setMessage(message)
            }
            setCancelable(isCancelable)

            icon?.apply { setIcon(this) }


            positiveButtonAction?.apply {
                setPositiveButton(
                    positiveButtonText ?: context.getString(R.string.okay)
                ) { dialogInterface, _ ->
                    invoke(dialogInterface)
                }

            }

            negativeButtonAction?.apply {
                setNegativeButton(
                    negativeButtonText ?: context.getString(R.string.cancel)
                ) { dialogInterface, _ ->
                    invoke(dialogInterface)
                }
            }

        }

        val dialog = builder.create()

        dialog.show()

        val titleView = dialog.findViewById<TextView>(R.id.alertTitle)

        titleView?.setTypeface(null, Typeface.BOLD)



    }

}


fun Fragment.showMaterialDialog(attributesBuilder: DialogAttributesBuilder.() -> Unit) {
    requireActivity().showMaterialDialog(attributesBuilder)
}

fun Activity.showMaterialDialog(attributesBuilder: DialogAttributesBuilder.() -> Unit) {

    val dialogBuilder = DialogAttributesBuilder(this).apply(attributesBuilder)

    dialogBuilder.build()

}

