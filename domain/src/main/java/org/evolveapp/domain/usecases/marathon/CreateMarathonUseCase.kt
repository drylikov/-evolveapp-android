package org.evolveapp.domain.usecases.marathon

import org.evolveapp.domain.repositories.MarathonsRepository
import java.io.File

open class CreateMarathonUseCase(private val marathonsRepository: MarathonsRepository) {

    fun launch(
        name: String,
        startDate: String,
        endDate: String,
        description: String,
        category: String,
        isPublic: Boolean,
        isActive: Boolean,
        avatar: File
    ) = marathonsRepository.createMarathon(
        name,
        startDate,
        endDate,
        description,
        category,
        isPublic,
        isActive,
        avatar
    )

}