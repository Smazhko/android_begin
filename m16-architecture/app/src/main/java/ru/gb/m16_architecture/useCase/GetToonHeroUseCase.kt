package ru.gb.m16_architecture.useCase

import ru.gb.m16_architecture.data.ToonHeroDTO
import ru.gb.m16_architecture.data.ToonHeroRepository
import javax.inject.Inject
import kotlin.random.Random

class GetToonHeroUseCase @Inject constructor(
    private val toonHeroRepository: ToonHeroRepository
) {

    suspend fun execute(): ToonHeroDTO {
        val heroId = Random.nextInt(1,826)
        return toonHeroRepository.getToonHero(heroId)
    }
}