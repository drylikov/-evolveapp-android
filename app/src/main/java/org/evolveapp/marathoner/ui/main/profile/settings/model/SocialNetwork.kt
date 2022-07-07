package org.evolveapp.marathoner.ui.main.profile.settings.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SocialNetwork(
    @StringRes
    val name: Int,

    val isConnected: Boolean,

    @DrawableRes
    val logo: Int,

    )