package org.evolveapp.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.evolveapp.domain.models.categories.Category
import org.evolveapp.domain.models.marathon.participants.MarathonParticipants
import org.evolveapp.domain.models.wrapper.ApiResponse
import java.io.File

interface MarathonsRepository {

    fun getParticipants(id: String): Flow<ApiResponse<MarathonParticipants>>

    fun createMarathon(
        name: String,
        startDate: String,
        endDate: String,
        description: String,
        category: String,
        isPublic: Boolean,
        isActive: Boolean,
        avatar: File,
    ): Flow<ApiResponse<Any>>

    fun getCategories(): Flow<ApiResponse<List<Category>>>

}