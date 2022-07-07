package org.evolveapp.domain.usecases.marathon

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.evolveapp.domain.models.marathon.participants.MarathonParticipants
import org.evolveapp.domain.models.marathon.participants.Participant
import org.evolveapp.domain.models.marathon.participants.User
import org.evolveapp.domain.models.wrapper.ApiResponse
import org.evolveapp.domain.repositories.MarathonsRepository

class GetParticipantsUseCase(private val marathonsRepository: MarathonsRepository) {

    fun launch(id: String) = marathonsRepository.getParticipants(id)

    /**
     * Use this function only for testing purpose
     */
    fun launchCustomResponse(
        delay: Long = 2000,
        friendsCount: Int,
        participantsCount: Int,
    ): Flow<ApiResponse<MarathonParticipants>> {
        return flow {

            delay(delay)

            val friends = arrayListOf<Participant>()
            val participants = arrayListOf<Participant>()

            repeat(friendsCount){
                friends.add(Participant(user = User(
                    id = -5, firstName = "Demo", lastName = "User", avatar = "https://via.placeholder.com/300/F00"
                )))
            }

            repeat(participantsCount){
                participants.add(Participant(user = User(
                    id = -5, firstName = "Demo", lastName = "User", avatar = "https://via.placeholder.com/300/F00"
                )))
            }

            val response = ApiResponse(
                success = true,
                statusMessage = "success",
                statusCode = 200,
                data = MarathonParticipants(
                    friends = friends,
                    participants = participants
                )
            )

            emit(response)

        }
    }

}