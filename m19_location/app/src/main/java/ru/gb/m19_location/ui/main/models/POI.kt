package ru.gb.m19_location.ui.main.models

data class POI(
    val type: String,
    val geometry: Geometry,
    val properties: Properties
) {
    data class Geometry(
        val type: String,
        val coordinates: List<Double>
    )

    data class Properties(
        val xid: String,
        val name: String,
        val rate: Int,
        val osm: String,
        val wikidata: String,
        val kinds: String
    )
}
