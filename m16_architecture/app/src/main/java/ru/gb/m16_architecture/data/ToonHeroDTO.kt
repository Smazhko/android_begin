package ru.gb.m16_architecture.data

import ru.gb.m16_architecture.entity.ToonHero

data class ToonHeroDTO (
    override val id: Int,
    override val name: String,
    override val image: String,
    override val status: String
) : ToonHero{
    val description: String
        get() = "ID: $id\nName: $name\nStatus: $status"
}
