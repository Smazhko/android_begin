package ru.gb.m16_architecture.data

import javax.inject.Inject

class ToonHeroRepository @Inject constructor(private val toonHeroDataSource: ToonHeroDataSource) {
    suspend fun getToonHero(heroId: Int): ToonHeroDTO {
        return toonHeroDataSource.loadToonHeroInfo(heroId)
    }
}