package ru.gb.m15_room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM words ORDER BY word ASC")
    fun getAll(): Flow<List<Word>>

    @Query("SELECT * FROM words ORDER BY word ASC LIMIT 5")
    fun getFirstTen(): Flow<List<Word>>

    @Query("SELECT * FROM words WHERE word = :word LIMIT 1")
    suspend fun getWord(word: String): Word?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    @Query("DELETE FROM words")
    suspend fun deleteAll()

    @Update
    suspend fun update(word: Word)
}