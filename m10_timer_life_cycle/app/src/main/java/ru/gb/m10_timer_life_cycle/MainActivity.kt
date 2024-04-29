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

    private var fullTime: Int = 20
    private var remainTime: Int = 20
    private var isTimerRunning = false

    private var countdownThread: CountdownThread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let{
            Log.d("ON-CREATE", it.toString())
        }

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

        savedInstanceState?.let { bundle ->
            Log.d("MAIN", "ПЕРЕВОРОТ **********************************")
            fullTime = bundle.getInt("fullTime")
            remainTime = bundle.getInt("remainTime")
            isTimerRunning = bundle.getBoolean("isTimerRunning")
            if (isTimerRunning) startTimer()
            Log.d("ПЕРЕВОРОТ", "remainTime: $remainTime | fullTime: $fullTime | $isTimerRunning")
        }

        slider.value = fullTime.toFloat()
        counterTextView.text = remainTime.toString()
        progressBar.max = fullTime
        progressBar.setProgress(remainTime)

        slider.addOnChangeListener { _, value, _ ->
            fullTime = value.toInt()
            remainTime = value.toInt()
            counterTextView.text = fullTime.toString()
            progressBar.max = fullTime
            progressBar.setProgress(fullTime)
        }


        startButton.setOnClickListener {
            if (!isTimerRunning) {
                Log.d("123", ">>>>>>>>>нажата кнопка СТАРТ")
                startTimer()
            } else {
                Log.d("123", ">>>>>>>>>> нажата кнопка СТОП")
                stopTimer()
            }
            Log.d("123", "isTimerRunning = " + isTimerRunning.toString())
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("fullTime", fullTime)
        outState.putInt("remainTime", remainTime)
        outState.putBoolean("isTimerRunning", isTimerRunning)
        super.onSaveInstanceState(outState)
        countdownThread?.interrupt()
        countdownThread = null
        Log.d("ДО ПЕРЕВОРОТА", "СОХРАНЕНИЕ: remainTime: $remainTime | fullTime: $fullTime | $isTimerRunning")
    }

    override fun onPause() {
        super.onPause()
        countdownThread?.interrupt()
        countdownThread = null
    }

    private fun startTimer() {
        isTimerRunning = true

        startButton.textSize = 32F
        startButton.text = getString(R.string.stop)
        slider.isEnabled = false
        slider.value = fullTime.toFloat()
        counterTextView.text = remainTime.toString()
        progressBar.max = fullTime
        progressBar.setProgress(remainTime)

        Log.d("RUNThread", "remainTime: $remainTime | fullTime: $fullTime")

        countdownThread = CountdownThread()
        countdownThread?.start()
    }

    private fun stopTimer() {
        countdownThread?.interrupt()
        countdownThread = null

        // сбрасываем переменные
        isTimerRunning = false
        fullTime = slider.value.toInt()
        remainTime = fullTime

        // сбрасываем элементы в начальное состояние
        startButton.textSize = 26F
        startButton.text = getString(R.string.start)
        slider.isEnabled = true
        counterTextView.text = fullTime.toString()
        progressBar.max=fullTime
        progressBar.setProgress(fullTime)
        Log.d("stopThread", "remainTime: $remainTime | fullTime: $fullTime")
    }

    private inner class CountdownThread() : Thread() {
        override fun run() {
            var tag: String = Thread.currentThread().name
            val maxSeconds = fullTime
            val countdownHandler = Handler(Looper.getMainLooper())
            var secondsRemaining = remainTime

            while (secondsRemaining >= 0 && isTimerRunning) {
                countdownHandler.post {
                    counterTextView.text = secondsRemaining.toString()
                    progressBar.setProgress(maxSeconds - secondsRemaining, true)
                }
                // Log.d(tag, "counterTextView: ${counterTextView.hashCode()} | progressBar: ${progressBar.hashCode()}")

                try {
                    sleep(1000) // Пауза на 1 секунду
                } catch (e: InterruptedException) {
                    // Прерывание потока, если пользователь остановил таймер
                    return
                }

                secondsRemaining--
                remainTime = secondsRemaining
                Log.d(tag, "secondsRemaining: $secondsRemaining  | remainTime: $remainTime | maxSeconds: $maxSeconds")
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