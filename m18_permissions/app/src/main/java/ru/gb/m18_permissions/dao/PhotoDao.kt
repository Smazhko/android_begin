package ru.gb.m18_permissions.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.gb.m18_permissions.model.Photo

@Dao
interface PhotoDao {
    @Insert
    suspend fun insert(photo: Photo)

    @Query("SELECT * FROM photos ORDER BY date DESC")
    fun getAllPhotos(): LiveData<List<Photo>>
}