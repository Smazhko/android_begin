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
    private val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd.MM.YYYY")


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

        binding.buttonStart.setOnClickListener {
            findNavController().navigate(R.id.action_Title_to_Questions)
            val localTime = LocalTime.now()
            val hour = localTime.hour
            val minute = localTime.minute
            val time_hours = resources.getQuantityString(R.plurals.count_hours, hour, hour)
            val time_minutes = resources.getQuantityString(R.plurals.count_minutes, minute, minute)
            val text = getString(R.string.label_time_now) + ": $time_hours $time_minutes"
            Snackbar.make(binding.buttonStart, text, Snackbar.LENGTH_SHORT).show()
        }

        binding.buttonDate.setOnClickListener {
            // чтобы выставить календарь в предыдущую выбранную дату
            // чтобы дать форме календаря команду открыться на нудном месяце
            val constraintCalend = CalendarConstraints.Builder()
                .setOpenAt(calendar.timeInMillis)
                .build()

            val dateDialog = MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintCalend) // чтобы выставить календарь в предыдущую выбранную дату
                .setTitleText(resources.getString(R.string.choose_date))
                .build()
            dateDialog.addOnPositiveButtonClickListener { timeInMillis ->
                    calendar.timeInMillis =  timeInMillis
//                    val day = calendar.get(Calendar.DAY_OF_MONTH)
//                    val month = calendar.get(Calendar.MONTH) + 1
//                    val year = calendar.get(Calendar.YEAR)
//                val text = "$day.$month.$year"
//                Snackbar.make(binding.buttonDate, text, Snackbar.LENGTH_SHORT).show()
                Snackbar.make(binding.buttonDate, dateFormat.format(calendar.time), Snackbar.LENGTH_SHORT).show()

                }
            dateDialog.show(childFragmentManager, "GatePicker") // ы активити используется supportFragmentManager
        }
    }

    override fun onStop(){
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