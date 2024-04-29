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
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    val scope = CoroutineScope(Dispatchers.Main)
//    CoroutineScope - это область, в которой можно запускать корутины. Он предоставляет контекст
//    выполнения для корутин. Корутины, запущенные в пределах определенной области, будут
//    автоматически отменены при завершении этой области.
//    CoroutineScope предоставляет управление временем жизни корутин и их обработку. Обычно область
//    корутины создается вместе с активностью, фрагментом или другим компонентом приложения и
//    связывается с их жизненным циклом. Когда активность или фрагмент завершает свою работу,
//    все запущенные в ней корутины автоматически отменяются, что помогает избежать утечек ресурсов
//    и других проблем.


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

        // восстанавливаем сохраненные в Bundle значения переменных по ключам.
        // ATOMIC типы в Bundle размещаться не могут.
        // сразу проверяем - если поток со временем был запущен, то запускаем его снова,
        // так как предыдущий был уже остановлен (может остановиться автоматически при закрытии
        // или обновлении активити)
        savedInstanceState?.let { bundle ->
            Log.d("MAIN", "ПЕРЕВОРОТ **********************************")
            fullTime = bundle.getInt("fullTime")
            remainTime = bundle.getInt("remainTime")
            isTimerRunning = bundle.getBoolean("isTimerRunning")
            if (isTimerRunning) startTimer()
            Log.d("ПЕРЕВОРОТ", "remainTime: $remainTime | fullTime: $fullTime | $isTimerRunning")
        }

        // обновляем состояние всех компонентов, состояние которых важно
        // при повторной перерисовке активити
        // тоже самое делаем при СТАРТЕ отсчёта
        slider.value = fullTime.toFloat()
        counterTextView.text = remainTime.toString()
        progressBar.max = fullTime
        progressBar.progress = remainTime

        // слушатель для слайдера - выбора времени для отсчёта
        // обновляем переменные и обновляем состояние прогресс-бара и его текста
        slider.addOnChangeListener { _, value, _ ->
            fullTime = value.toInt()
            remainTime = value.toInt()
            counterTextView.text = fullTime.toString()
            progressBar.max = fullTime
            progressBar.progress = fullTime
        }

        // единственная кнопка, которая меняет свой текст в зависимости от состояния
        startButton.setOnClickListener {
            // если ЕЩЕ НЕ запущен
            if (!isTimerRunning) {
                Log.d("123", ">>>>>>>>>нажата кнопка СТАРТ")
                startTimer()
                // Делаем кнопку недоступной после нажатия, потому что если часто щёлкать -
                // глючит анимация прогресс-бара, и
                // запускаем корутину для задержки выполнения кода на 900 миллисекунд
                startButton.isEnabled = false
                scope.launch {
                    delay(900)
                    // После задержки делаем кнопку доступной
                    startButton.isEnabled = true
                }
            } else { // если УЖЕ запущен
                Log.d("123", ">>>>>>>>>> нажата кнопка СТОП")
                stopTimer()
            }
            Log.d("123", "isTimerRunning = " + isTimerRunning.toString())
        }

    }

    // сохраняем состояние активити. чтобы передать его во вновь отрисованную.
    // сначала заполняем outState, а потом вызываем super
    // на всякий пожарный останавливаем поток, чтоб он не работал в фоне
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("fullTime", fullTime)
        outState.putInt("remainTime", remainTime)
        outState.putBoolean("isTimerRunning", isTimerRunning)
        super.onSaveInstanceState(outState)
        countdownThread?.interrupt()
        countdownThread = null
        Log.d("ДО ПЕРЕВОРОТА", "СОХРАНЕНИЕ: remainTime: $remainTime | fullTime: $fullTime | $isTimerRunning")
    }

    // при исчезновении активити с экрана (перерисовка или после кнопки НАЗАД останавливаем
    // уже ненужные потоки
    override fun onPause() {
        super.onPause()
        countdownThread?.interrupt()
        countdownThread = null
    }

    // обновляем переменные, состояния ВСЕХ(!!!!) элементов по этим главным переменным.
    // если что-то не обновить, откуда ни возьми наёдутся переменные по-умолчанию  и всё испортят.
    // создаём и запускаем ПОТОК отсчёта времени.
    // передавать в поток данные смысла нет, так как по необходимости мы их оттуда
    // не заберём, поэтому пусть поток пользуется глобальными переменными класса
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
        // прерываем поток
        countdownThread?.interrupt()
        countdownThread = null

        // сбрасываем переменные
        isTimerRunning = false
        fullTime = slider.value.toInt()
        remainTime = fullTime

        // сбрасываем ВСЕ!!! элементы в начальное состояние
        startButton.textSize = 26F
        startButton.text = getString(R.string.start)
        slider.isEnabled = true
        counterTextView.text = fullTime.toString()
        progressBar.max=fullTime
        progressBar.setProgress(fullTime)

        // запасная перерисовка компонента на случай глюка анимации. у других компонентов
        // за перериосовку будут отвечать другие очень разнгые по названиям  методы
        progressBar.requestLayout()

        Log.d("stopThread", "remainTime: $remainTime | fullTime: $fullTime")
    }

    // внутренний класс ПОТОК отсчёта времени
    private inner class CountdownThread() : Thread() {
        override fun run() {
            var tag: String = Thread.currentThread().name // переменная для ЛОГа
            val maxSeconds = fullTime // создаём копии переменных для внутреннего пользования
            var secondsRemaining = remainTime

            // Handler нужен для изменения параметров жлементов НЕ из Активити, а из потока.
            val countdownHandler = Handler(Looper.getMainLooper())

            // пока есть куда считать и пока не нажали кнопку СТОП - перерисовываем элементы
            // и отнимаем счётчик
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

            // (!!!!) ==> Сброс состояния после завершения таймера
            countdownHandler.post {
                stopTimer()
            }
        }
    }
}