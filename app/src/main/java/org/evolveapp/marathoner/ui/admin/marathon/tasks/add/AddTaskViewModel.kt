package org.evolveapp.marathoner.ui.admin.marathon.tasks.add

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.evolveapp.domain.usecases.marathon.tasks.AddTaskUseCase
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
) : ViewModel() {

    val title = MutableStateFlow("")
    val description = MutableStateFlow("")
    val mandatory = MutableStateFlow(false)

    fun switchMandatoryState() {
        mandatory.value = !mandatory.value
    }


}