package org.evolveapp.marathoner.ui.participants

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.evolveapp.domain.usecases.marathon.GetParticipantsUseCase
import org.evolveapp.domain.usecases.profile.SendFriendRequestUseCase
import javax.inject.Inject

@HiltViewModel
class ParticipantsViewModel @Inject constructor(
    private val getParticipantsUseCase: GetParticipantsUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
) : ViewModel() {

    var marathonId: String? = null

    fun getParticipants(id: String) = getParticipantsUseCase.launch(id)

    /*fun getParticipants(id: String) = getParticipantsUseCase.launchCustomResponse(
        delay = 2000,
        friendsCount = 8,
        participantsCount = 50
    )*/


    fun sendFriendRequest(userId: String) = sendFriendRequestUseCase.launch(userId)


}