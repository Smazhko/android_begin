package ru.gb.myapplication

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.gb.myapplication.databinding.ActivityMainBinding


/*
типы ресурсов  - как писать XML
https://microsin.net/programming/android/android-application-resource-types.html

урок 1: TextView Button ImageButton ViewBinding
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // внедряем использованием в последующем коде подхода с ViewBinding
        // чтобы в дальнейшем не использовать с каждым компонентом findViewById
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var passengerCounter = 0
        val capacity = 10
        binding.btnPlus.isEnabled = true
        binding.btnMinus.isEnabled = false
        binding.txtCapacity.text = getString(R.string.capacity) + ": " + capacity.toString()

        binding.btnPlus.setOnClickListener{
                passengerCounter++
                binding.remainCounter.text = (capacity - passengerCounter).toString()
                binding.totalCounter.text = passengerCounter.toString()
            if (passengerCounter < capacity) {
                binding.message.text = getString(R.string.txt_remained)
                binding.message.setTextAppearance(R.style.text_remained)
                binding.remainCounter.visibility = View.VISIBLE
                binding.btnMinus.isEnabled = true
            }
            else {
                binding.btnPlus.isEnabled = false
                binding.btnReset.visibility = View.VISIBLE
                binding.btnMinus.isEnabled = false
                binding.remainCounter.visibility = View.INVISIBLE
                binding.message.text = getString(R.string.txt_full)
                binding.message.setTextAppearance(R.style.text_full)
            }
        }

        binding.btnMinus.setOnClickListener {
                passengerCounter--
                binding.totalCounter.text = passengerCounter.toString()
                binding.remainCounter.text = (capacity - passengerCounter).toString()
            if (passengerCounter > 0) {
                binding.message.text = getString(R.string.txt_remained)
                binding.message.setTextAppearance(R.style.text_remained)
                binding.remainCounter.visibility = View.VISIBLE
                binding.btnPlus.isEnabled = true
            }
            else {
                binding.btnMinus.isEnabled = false
                binding.remainCounter.visibility = View.INVISIBLE
                binding.message.text = getString(R.string.txt_free)
                binding.message.setTextAppearance(R.style.text_free)
            }
        }

        binding.btnReset.setOnClickListener {
            passengerCounter = 0
            binding.btnMinus.isEnabled = false
            binding.btnPlus.isEnabled = true
            binding.totalCounter.text = "0"

            binding.message.text = getString(R.string.txt_free)
            binding.message.setTextAppearance(R.style.text_free)
            binding.remainCounter.visibility = View.INVISIBLE
            binding.btnReset.visibility = View.INVISIBLE
        }

    }
}


