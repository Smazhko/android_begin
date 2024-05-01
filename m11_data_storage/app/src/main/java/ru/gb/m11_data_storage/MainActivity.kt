package ru.gb.m11_data_storage

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.gb.m11_data_storage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val input = binding.inputText

        binding.btnSaveLocalVar.setOnClickListener {
            val text = input.text.toString()
            if (text.isNotEmpty())
                repository.saveTextToLocalVar(this, text)
            else
                Toast.makeText(this, "Текст пуст - добавлять нечего", Toast.LENGTH_SHORT).show()
        }

        binding.btnSaveSharPerf.setOnClickListener{
            val text = input.text.toString()
            if (text.isNotEmpty())
                repository.saveTextToSharedPrefs(this, text)
            else
                Toast.makeText(this, "Текст пуст - добавлять нечего", Toast.LENGTH_SHORT).show()
        }

        binding.btnSaveFile.setOnClickListener{
            val text = input.text.toString()
            if (text.isNotEmpty())
                repository.saveTextToFile(this, text)
            else
                Toast.makeText(this, "Текст пуст - добавлять нечего", Toast.LENGTH_SHORT).show()
        }

        binding.btnClearAll.setOnClickListener {
            repository.clearAll(this)
            binding.outputText.text = ""
            Toast.makeText(this, "Все источники данных очищены", Toast.LENGTH_SHORT).show()
        }

        binding.btnOpen.setOnClickListener{
            binding.outputText.text = repository.getText(this)
        }
    }
}

/*
Создать одноэкранное приложение, которое позволяет вводить данные и записывать их в SharedPreference,
а также отображать введённые данные даже после повторного открытия приложения.
1. Создайте экран произвольной вёрстки, аналогичный тому, который вы разрабатывали ранее со спикером, содержащий:
- поле для ввода текста (EditText);
- кнопку c текстом «Сохранить» (Button);
- кнопку с текстом «Очистить» (Button);
- текстовое поле (TextView).
2. Создайте класс Repository, который будет содержать несколько методов:
getDataFromSharedPreference(): String? — будет доставать значение из SharedPreference;
getDataFromLocalVariable(): String? — будет доставать значение, возвращать значение локальной переменной;
saveText(text: String) — будет записывать значения и в SharedPreference, и в локальную переменную.
clearText() — будет очищать значение и в SharedPreference, и в локальной переменной.
getText(): String — будет доставать значение из источников: сначала попытается взять значение
локальной переменной; если оно null, то попытаемся взять значение из SharedPreference.
3. Создайте экземпляр класса репозитория в своём fragment/activity.
4. После нажатия кнопки «Сохранить» — возьмите текстовое значение из EditText и передайте его в
метод saveText(text: String).
5. При нажатии кнопки «Очистить» — очистите значение в репозитории через метод clearText().
6. При открытии приложения или при изменении значения текст в TextView должен меняться.


Что оценивается
- Значение из EditText записывается в переменную и SharedPreference после нажатия на кнопку «Сохранить».
- После сохранения — текст в TextView меняется на тот, который ранее ввели в EditText.
- При повторном старте приложения отображается значение, которое мы записали.
- При нажатии кнопки «Очистить» значение в репозитории меняется на null, а TextView отображает « » (пустая строка).
- Нет багов и вылетов.
- Код чистый, у переменных и компонентов понятные названия, соблюдаются принципы ООП.


Советы и рекомендации
- Методы getDataFromSharedPreference() и getDataFromLocalVariable() должны быть приватными (private).
- Всё взаимодействие c репозиторием должно идти через методы saveText, getText и clearText.
- Если приложение не запускается, постарайтесь внимательно проанализировать информацию об ошибке
и добавить нужное исправление.
 */