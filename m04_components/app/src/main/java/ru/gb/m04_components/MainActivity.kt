package ru.gb.m04_components

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import ru.gb.m04_components.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var isNameExist = false
    private var isPhoneExist = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val points = Random.nextInt(101)
        bind.progrBar.progress = points
        bind.txtProgrBarPercent.text = "$points/100"

        val txtInputName = bind.txtInputName
        val txtInputPhone = bind.txtInputPhone
        val switcher = bind.switchNotifications
        val chbxAutorization = bind.chbxNotifyAuthorization
        val chbxNews = bind.chbxNotifyNews
        val btnSave = bind.btnSave

        txtInputName.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (text.length > 40) {
                    txtInputName.setText(text.toString().substring(0, 40))
                    txtInputName.setSelection(40)
                }
                if (text.isNotEmpty())
                    isNameExist = true
                else
                    isNameExist = false
            } else isNameExist = false
            checkForm()
        }

        // попытка скрыть клавиатуру с экрана при начале взаимодействия с другими элементами - НЕУДАЧНАЯ
//
//        txtInputName.onFocusChangeListener =
//            View.OnFocusChangeListener { view, hasFocus ->
//                if (!hasFocus) {
//                    //txtInputName.clearFocus();
//                    // Скрытие клавиатуры
//                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.hideSoftInputFromWindow(view.windowToken, 0)
//                }
//            }



        txtInputPhone.doOnTextChanged { text, _, _, _ ->
            if(text != null){
                if (text.isNotEmpty())
                    isPhoneExist = true
                else
                    isPhoneExist = false
            } else isPhoneExist = false
            checkForm()
        }

        switcher.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                chbxAutorization.isEnabled = true
                chbxNews.isEnabled = true
            }
            else {
                chbxAutorization.isEnabled = false
                chbxNews.isEnabled = false
            }
        }

        btnSave.setOnClickListener {
            Toast.makeText(this, getString(R.string.info_saved), Toast.LENGTH_LONG).show()
        }


    }

    private fun checkForm() {
        if (isNameExist && isPhoneExist)
            findViewById<Button>(R.id.btnSave).isEnabled = true
        else
            findViewById<Button>(R.id.btnSave).isEnabled = false
    }

}


    /*

     */