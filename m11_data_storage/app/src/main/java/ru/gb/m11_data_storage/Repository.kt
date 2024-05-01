package ru.gb.m11_data_storage

import android.content.Context
import android.content.Context.*
import android.content.SharedPreferences
import android.widget.Toast
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

private const val PREFERENCE_NAME = "custom_shared_preference"
private const val PREFERENCE_KEY = "key_for_text"
private const val FILE_NAME = "data.txt"

class Repository {
    // контекст не сохраняем в локальную переменную класса,
    // потому что он может быть каждый раз разный.
    private var localValue: String? = null

    public fun saveTextToLocalVar(context: Context, textForSave: String) {
        // сохраняем в локальную переменную - ДОПИСЫВАЕМ К ЗНАЧЕНИЮ ПО-УМОЛЧАНИЮ
        localValue = textForSave
        Toast.makeText(context, "Текст сохранен в локальную переменную", Toast.LENGTH_SHORT).show()
    }

    public fun saveTextToSharedPrefs(context: Context, text: String) {
        val textForSave = text + "\n"

        // сохраняем в SharedPreference
        var pref = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putString(PREFERENCE_KEY, textForSave)
        editor.apply()
        Toast.makeText(context, "Текст сохранен в SharedPreferences", Toast.LENGTH_SHORT).show()
    }

    public fun saveTextToFile(context: Context, text: String) {
        val textForSave = text + "\n"

        // сохраняем в файл - ДОБАВЛЯЕМ К СУЩЕСТВУЮЩЕМУ
        var fileOutputStream: FileOutputStream? = null
        try {
            // если файла не существует, метод openFileOutput() создаст новый файл. В случае,
            // если файл уже существует, он будет дозаписан. В этом случае исключение не будет выброшено.
            fileOutputStream = context.openFileOutput(FILE_NAME, MODE_APPEND)
            fileOutputStream.write(textForSave.toByteArray())

            Toast.makeText(context, "Текст дописан в файл", Toast.LENGTH_SHORT).show()
        } catch (ex: IOException){
            Toast.makeText(context, "ОШИБКА: " + ex.message, Toast.LENGTH_SHORT).show()
        } finally {
            fileOutputStream?.close()
        }
    }

    public fun getText(context: Context): String {
        return when{
            getFromLocalVariable() != null -> "Текст из локальной переменной: \n" + getFromLocalVariable()!!
            getTextFromPreference(context) != null -> "Текст из SharedPreferences: \n" + getTextFromPreference(context)!!
            getTextFromFile(context) != null -> "Текст из файла: \n" + getTextFromFile(context)!!
            else -> "Ни один источник не содержит запрашиваемую информацию"
        }
    }

    private fun getTextFromPreference(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
        return prefs.getString(PREFERENCE_KEY, null)
    }

    private fun getTextFromFile(context: Context): String? {
        /*
        // Объявляется переменная fileInputStream типа FileInputStream? и инициализируется
        // значением null. Этот поток будет использоваться для чтения данных из файла.
        var fileInputStream: FileInputStream? = null

        return try {
            // Открывается входной поток файла с именем, указанным в FILE_NAME, используя контекст
            // приложения. Функция openFileInput возвращает FileInputStream, который связан с указанным файлом.
            fileInputStream = context.openFileInput(FILE_NAME)

            //Создается массив байтов textInBytes размером, равным количеству байтов, доступных для чтения из fileInputStream.
            val textInBytes = ByteArray(fileInputStream.available())

            // Содержимое файла считывается в массив байтов textInBytes
            fileInputStream.read(textInBytes)
            Toast.makeText(context, "Текст получен из файла", Toast.LENGTH_LONG).show()

            // Массив байтов преобразуется в строку с использованием конструктора String,
            // и эта строка возвращается как результат выполнения блока try.
            String(textInBytes)
        } catch (ex: FileNotFoundException) {
            Toast.makeText(context, "Не удалось читать текст. Файл не существует!", Toast.LENGTH_LONG).show()
            return null
        } catch (ex: IOException){
            Toast.makeText(context, "ОШИБКА: " + ex.message, Toast.LENGTH_LONG).show()
            return null
        } finally {
            fileInputStream?.close()
        }
        */

        // ещё один вариант считывания данных из файла с использованием USE, который автоматом закроет READER

        return try {
            // Для того чтобы открыть именно тот файл, который мы сохранили,
            // надо обращаться к нему через контекст.
            // Конструкция File(FILE_NAME) не находит нужный файл.
            context.openFileInput(FILE_NAME).bufferedReader().use { it ->
                val lines: StringBuilder = StringBuilder()
                it.forEachLine { line ->
                    lines.append(line)
                    lines.append("\n")
                }
                // удаляем последний лишний знак переноса строки
                lines.deleteCharAt(lines.lastIndex)
                Toast.makeText(context, "Текст получен из файла", Toast.LENGTH_SHORT).show()
                String(lines)
            }
        } catch (ex: FileNotFoundException) {
            Toast.makeText(context, "Не удалось читать текст. Файл не существует!", Toast.LENGTH_LONG).show()
            return null
        } catch (ex: IOException){
            Toast.makeText(context, "ОШИБКА: " + ex.message, Toast.LENGTH_SHORT).show()
            return null
        }
    }

    private fun getFromLocalVariable():String? {
        return localValue
    }

    public fun clearAll(context: Context){
        localValue = null

        // полностью очищаем SharedPreferences
        context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE).edit().clear().apply()

        // удаляем файл
        context.deleteFile(FILE_NAME)
    }
}
/*
2. Создайте класс Repository, который будет содержать несколько методов:
getDataFromSharedPreference(): String? — будет доставать значение из SharedPreference;
getDataFromLocalVariable(): String? — будет доставать значение, возвращать значение локальной переменной;
saveText(text: String) — будет записывать значения и в SharedPreference, и в локальную переменную.
clearText() — будет очищать значение и в SharedPreference, и в локальной переменной.
getText(): String — будет доставать значение из источников: сначала попытается взять значение
локальной переменной; если оно null, то попытаемся взять значение из SharedPreference.
*/