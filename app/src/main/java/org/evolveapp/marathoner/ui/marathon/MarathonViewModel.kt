package org.evolveapp.marathoner.ui.marathon

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MarathonViewModel @Inject constructor() : ViewModel() {

    var marathonId: String? = null

}