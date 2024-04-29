package ru.gb.m10_timer_life_cycle

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.slider.Slider
import ru.gb.m10_timer_life_cycle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var counterTextView: TextView
    private lateinit var progressBar: CircularProgressIndicator
    private lateinit var slider: Slider
    private lateinit var startButton: MaterialButton

    private var countdownThread: CountdownThread? = null
    private var isTimerRunning = false

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

        counterTextView = binding.counter
        progressBar = binding.progressBar
        slider = binding.slider
        startButton = binding.btnStart

        slider.value = 20F
        progressBar.setProgress(slider.value.toInt())

        slider.addOnChangeListener { _, value, _ ->
            counterTextView.text = value.toInt().toString()
            progressBar.max=value.toInt()
            progressBar.setProgress(value.toInt())
        }

        startButton.setOnClickListener{
            if (!isTimerRunning) {
                Log.d("123", ">>>>>>>>>нажата кнопка СТАРТ")
                startTimer()
            } else {
                Log.d("123", ">>>>>>>>>> нажата кнопка СТОП")
                stopTimer()
            }
            Log.d("123", isTimerRunning.toString())
        }

    }

    private fun startTimer() {
        isTimerRunning = true
        countdownThread = CountdownThread()
        startButton.text = getString(R.string.stop)
        startButton.textSize = 32F
        slider.isEnabled = false
        progressBar.max = slider.value.toInt()
        progressBar.setProgress(slider.value.toInt())
        countdownThread?.start()
    }

    private fun stopTimer() {
        isTimerRunning = false
        countdownThread?.interrupt()
        countdownThread = null
        startButton.textSize = 26F
        startButton.text = getString(R.string.start)
        slider.isEnabled = true
        counterTextView.text = slider.value.toInt().toString()
        progressBar.setProgress(slider.value.toInt())
    }

    private inner class CountdownThread : Thread() {
        override fun run() {
            val maxSeconds = slider.value.toInt()
            val countdownHandler = Handler(Looper.getMainLooper())
            var secondsRemaining = maxSeconds

            while (secondsRemaining >= 0 && isTimerRunning) {
                countdownHandler.post {
                    counterTextView.text = secondsRemaining.toString()
                    progressBar.setProgress(maxSeconds - secondsRemaining, true)
                }

                try {
                    sleep(1000) // Пауза на 1 секунду
                } catch (e: InterruptedException) {
                    // Прерывание потока, если пользователь остановил таймер
                    return
                }

                secondsRemaining--
            }

            // Сброс состояния после завершения таймера
            countdownHandler.post {
                stopTimer()
            }
        }
    }

}

/*

                val handler = Handler(Looper.getMainLooper())
                thread{
                    handler.post{
                        for (i in )
                        sleep(1_000)

                    }

                }
 */