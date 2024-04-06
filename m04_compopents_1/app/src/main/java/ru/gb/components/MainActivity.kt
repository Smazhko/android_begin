package ru.gb.components

import android.app.UiModeManager.MODE_NIGHT_YES
import android.content.res.Resources
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.snackbar.Snackbar
import ru.gb.components.databinding.ActivityMainBinding

private const val NONE = -1

class MainActivity : AppCompatActivity() {
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

        // очистка выбора radioButtonGroup по нажатию на кнопку
        binding.btnReset.setOnClickListener {
            binding.rgroup1.clearCheck()
        }

        // вывод всплывающего сообщения по выбору из радио-кнопок
        binding.rgroup1.setOnCheckedChangeListener { _, buttonId ->
            when (buttonId) {
                R.id.rb1 -> showSnackbar("1")
                R.id.rb2 -> showSnackbar("2")
                R.id.rb3 -> showSnackbar("3")
                NONE -> showSnackbar("---")
            }
        }

        // проверка логина и пароля из EditText
        binding.btnLogin.setOnClickListener {
            val login = binding.editLogin.text
            val pass = binding.editPassword.text
            if (login.isNotBlank() && pass.isNotBlank()) {
                showSnackbar("SUCCESS")
            } else {
                showSnackbar("FAIL")
            }
        }

        // контролируемый ввод адреса электронной почты
        val emailTxtInput = binding.txtInputEmail
        val emailTxtInputLayout = binding.txtInputLayoutEmail
        emailTxtInput.doOnTextChanged { text, start, before, count ->
            if (isEmailValid(text)) {
                emailTxtInputLayout.isErrorEnabled = false
            } else {
                emailTxtInputLayout.error = "Некорректный email"
                emailTxtInputLayout.isErrorEnabled = true
            }
        }

        // смена темы по кнопке
        // не забываем сменить палитру светлой и тёмной темы в файлах THEMES.XML,
        // которые лежат в папках resources/values  и resources/values-night (если смотреть на проект в режиме PROJECT)
        // или вариант лучше - в каждой из папок задать свой файл COLORS.XML,
        // где прописать названия цветов (такие, какие должны быть в ТЕМЕ - primary и т.д.)
        binding.btnChangeTheme.setOnClickListener {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        // ВНОСИМ ИЗМЕНЕНИЯ В ДИЗАЙН ПОЛОСЫ ПРОГРЕССА

        val progress: LinearProgressIndicator =
            findViewById(R.id.progress)
// Выставляем определённый/неопределённый прогресс android:indeterminate
        progress.isIndeterminate = false
// Выставляем значение цвета фона app:trackColor
        progress.trackColor = ContextCompat.getColor(
            this,
            R.color.track_color
        )
// Выставляем значение цвета индикатора прогресса app:indicatorColor
        progress.setIndicatorColor(
            ContextCompat.getColor(
                this,
                R.color.indicator_color
            )
        )
// Выставляем значение закругления прогресса app:trackCornerRadius
        progress.trackCornerRadius =
            resources.getDimension(R.dimen.track_corner_radius).toInt()
// Выставляем значение ширины прогресса app:trackThickness
        progress.trackThickness =
            resources.getDimension(R.dimen.track_thickness).toInt()

//        Помимо объявления размерности в dimens, вы можете получить их значение в
//        коде из Activity — так же, как и в случае со строками и цветами.
//        сначала нужно получить экземпляр класса ресурсов с помощью свойства resources, затем вызвать метод
//        getDimension, передав в качестве аргумента идентификатор ресурса dimen.
        val res: Resources = resources
        val dimenValue: Float =
            res.getDimension(R.dimen.track_thickness)

    }

    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun isEmailValid(email: CharSequence?): Boolean {
        return !email.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}