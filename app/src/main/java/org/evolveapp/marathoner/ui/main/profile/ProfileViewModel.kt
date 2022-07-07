package org.evolveapp.marathoner.ui.main.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.evolveapp.domain.usecases.friends.GetFriendsRequestsUseCase
import org.evolveapp.domain.usecases.friends.GetFriendsUseCase
import org.evolveapp.domain.usecases.profile.GetProfileIeUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileIeUseCase: GetProfileIeUseCase,
    private val getFriendsUseCase: GetFriendsUseCase,
    private val getFriendsRequestsUseCase: GetFriendsRequestsUseCase,
) : ViewModel() {

    fun getProfile() = getProfileIeUseCase.launch()

    fun getFriends() = getFriendsUseCase.launch()

    fun getFriendsRequests() = getFriendsRequestsUseCase.launch()

}