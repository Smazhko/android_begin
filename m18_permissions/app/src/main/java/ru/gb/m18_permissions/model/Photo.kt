package ru.gb.m18_permissions.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val filePath: String,
    val date: Long
)