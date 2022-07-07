package org.evolveapp.marathoner.ui.main.profile.friends

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.evolveapp.domain.usecases.friends.GetFriendsRequestsUseCase
import org.evolveapp.domain.usecases.friends.GetFriendsUseCase
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val getFriendsRequestsUseCase: GetFriendsRequestsUseCase,
) : ViewModel() {

    fun getFriends() = getFriendsUseCase.launch()

    fun getFriendsRequests() = getFriendsRequestsUseCase.launch()

}