package org.evolveapp.domain.usecases.marathon

import org.evolveapp.domain.repositories.MarathonsRepository

class GetCategoriesUseCase(private val marathonsRepository: MarathonsRepository) {

    fun launch() = marathonsRepository.getCategories()

}