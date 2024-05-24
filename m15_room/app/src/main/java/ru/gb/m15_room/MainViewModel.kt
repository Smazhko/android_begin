package ru.gb.m15_room

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "VIEW MODEL"
class MainViewModel(private val wordDao: WordDao) : ViewModel() {

    val allWords = this.wordDao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val firstWords = this.wordDao.getFirstTen()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun addWord(wordText: String){
        viewModelScope.launch{
            try {
                val existingWord = withContext(Dispatchers.IO) {wordDao.getWord(wordText)}
                if (existingWord == null) {
                    // Слово не найдено, добавляем новое слово
                    val newWord = Word(word = wordText, rate = 1)
                    Log.d(TAG, "existingWord: $existingWord\nnewWord:$newWord")
                    wordDao.insert(newWord)
                } else {
                    // Слово найдено, увеличиваем счётчик
                    val updatedWord = existingWord.copy(rate = existingWord.rate + 1)
                    Log.d(TAG, "updatedWord: $updatedWord")
                    wordDao.update(updatedWord)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to add word", e)
            }
        }
    }

    fun deleteAll(){
        viewModelScope.launch {
            wordDao.deleteAll()
        }
    }

}