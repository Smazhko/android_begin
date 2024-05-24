package ru.gb.m15_room

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ru.gb.m15_room.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val wordDao = (application as App).db.wordDao()
                return MainViewModel(wordDao) as T
            }
        }
    }

    private var previousWords: List<Word> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.inputTextField.doOnTextChanged {text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding.btnAdd.isEnabled = false
            }else {
                binding.btnAdd.isEnabled = true
            }
        }

        binding.btnAdd.setOnClickListener {
            var wordText = binding.inputTextField.text.toString()
            val regex = "^[а-яА-Яa-zA-Z-]+$".toRegex()

            if (wordText.isEmpty() || !regex.matches(wordText)) {
                Toast.makeText(this, getString(R.string.wrong_input), Toast.LENGTH_SHORT).show()

                // Удаляем все недопустимые символы
                wordText = wordText.filter { it.isLetter() || it == '-' }
                binding.inputTextField.setText(wordText) // Обновляем текст в EditText
            } else {
                viewModel.addWord(wordText)
                binding.inputTextField.text?.clear()
            }
        }

        binding.btnDelete.setOnClickListener { viewModel.deleteAll() }

        lifecycleScope.launch{
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.firstWords
                        .collect{words ->
                            if (words != previousWords) {
                                previousWords = words
                                binding.txtDb.text = words.joinToString(separator = "\r\n")
                            }
                        }
                }
            }
    }
}