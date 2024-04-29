package ru.gb.flagquiz

import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import ru.gb.flagquiz.databinding.FragmentTitleBinding
import java.text.SimpleDateFormat
import java.time.LocalTime

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentTitle.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentTitle : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // для взаимодействия с формой календаря вводим переменную КАЛЕНДАРЬ
    private val calendar = Calendar.getInstance()
    // а также шаблон для вывода даты
    // тоже самое можно со временем
    val dateFormat = SimpleDateFormat("dd.MM.YYYY")

    // хитрый способ задать БИНДИНГ, чтобы его потом можно было обнулить
    private var _binding: FragmentTitleBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // слушатель нажатия кнопки СТАРТ
        binding.buttonStart.setOnClickListener {
            // переход к фрагменту с вопросами
            findNavController().navigate(R.id.action_Title_to_Questions)

            // вывод SNACK с временем начала игры в формате 10 ЧАСОВ 00 МИНУТ
            // для склонения слов по падежам в зависимости от числа используем PLURALS
            // которые прописаны в RES/VALUES/STRINGS - также поддаётся локализациии
            val localTime = LocalTime.now()
            val hour = localTime.hour
            val minute = localTime.minute
            // ниже - достаём PLURALS из XML и применяем к строке.
            // в зависимости от значения hour  поставится нужный падеж
            // hour в параметрах нужно указать ДВАЖДЫ
            val time_hours = resources.getQuantityString(R.plurals.count_hours, hour, hour)
            val time_minutes = resources.getQuantityString(R.plurals.count_minutes, minute, minute)
            val text = getString(R.string.label_time_now) + ": $time_hours $time_minutes"
            Snackbar.make(binding.buttonStart, text, Snackbar.LENGTH_SHORT).show()
        }

        binding.buttonDate.setOnClickListener {
            // чтобы выставить календарь в предыдущую выбранную дату
            // чтобы дать форме календаря команду открыться на нужном месяце
            val constraintCalend = CalendarConstraints.Builder()
                .setOpenAt(calendar.timeInMillis)
                .build()

            // Создаём диалог календаря в отдельной переменной, чтобы
            // сначала добавить обработчик события для подтверждения даты,
            // а уже потом уже вывести сам диалог на экран - show().
            // Создание диалога использует паттерн проектирования BUILDER,
            // с помощью которого можно ПОШАГОВО задавать параметры создаваемого объекта.
            // Соответственно - сначала идут несколько SET. а затем создем обхект командой BUILD.
            val dateDialog = MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintCalend) // чтобы выставить календарь в предыдущую выбранную дату
                .setTitleText(resources.getString(R.string.choose_date)) // устанавливаем заголовок диалога календаря
                .build()

            dateDialog.addOnPositiveButtonClickListener { timeInMillis ->
                // присваиваем локальной переменной нашего фрагмента calendar значение,
                // которое выбрано пользователем в диалоге календаря
                    calendar.timeInMillis =  timeInMillis
                // другой способ - вместо ШАБЛОНА - получить данные из календаря
//                    val day = calendar.get(Calendar.DAY_OF_MONTH)
//                    val month = calendar.get(Calendar.MONTH) + 1
//                    val year = calendar.get(Calendar.YEAR)
//                val text = "$day.$month.$year"
//                Snackbar.make(binding.buttonDate, text, Snackbar.LENGTH_SHORT).show()
                Snackbar.make(binding.buttonDate, dateFormat.format(calendar.time), Snackbar.LENGTH_SHORT).show()
                }
            // ВЫЗЫВАЕМ ДИАЛОГ КАЛЕНДАРЯ. так как мы работаем из фрагмента, то в параметрах SHOW
            // используем childFragmentManager
            // в активити жа используется supportFragmentManager
            // также указываем ТЕГ, по которому сможем потом к диалогу обращаться - "GatePicker"
            dateDialog.show(childFragmentManager, "GatePicker")
        }
    }

    override fun onStop(){
        // при закрытии фрагмента показываем анимацию ВЫХОДА, которая сохранена в res/anim
        val slideOutAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fragment_slide_out_toleft)
        requireView().startAnimation(slideOutAnimation)
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTitleBinding.inflate(inflater, container, false)

        // показываем анимацию входа фрагмента на экран - увеличение из центра, которая сохранена в res/anim
        val slideInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fragment_explode)
        _binding?.root?.startAnimation(slideInAnimation)

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentTitle.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentTitle().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}