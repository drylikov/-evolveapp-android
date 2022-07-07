package org.evolveapp.marathoner.ui.admin.marathon.tasks.manage

import androidx.annotation.IdRes

data class Tab(
    @IdRes val id: Int,
    val name: String,
    var notifications: Boolean,
)