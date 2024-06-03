package ru.gb.m16_architecture.data

import javax.inject.Inject

class ToonHeroDataSource @Inject constructor() {

    suspend fun loadToonHeroInfo(heroId: Int): ToonHeroDTO {
        return RetrofitInstance.getToonHeroAPI.getTooHero(heroId.toString())
    }
}