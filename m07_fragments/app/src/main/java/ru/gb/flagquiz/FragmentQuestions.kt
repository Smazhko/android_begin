package ru.gb.flagquiz

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import ru.gb.flagquiz.data.QuizStorage
import ru.gb.flagquiz.databinding.FragmentQuestionsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentQuestions.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentQuestions : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentQuestionsBinding? = null
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonToStart.setOnClickListener {
            clearAllRadioGroups()
            clearUserAnswers()
//            findNavController().popBackStack(R.id.QuestionsFrg, false)
            findNavController().navigate(R.id.action_Questions_to_Title)
        }

        binding.buttonToResult.setOnClickListener {
            val bundle = bundleOf(ARG_PARAM1 to getResultOfQuiz(),
                ARG_PARAM2 to QuizStorage.questions.size)
            clearAllRadioGroups()
            clearUserAnswers()
//            findNavController().popBackStack(R.id.QuestionsFrg, false)
//            findNavController().popBackStack(R.id.TitleFrg, true)
            findNavController().navigate(R.id.action_Questions_to_Result, bundle)
        }

        for (q in QuizStorage.questions) {
            // создаём экземпляр вьюшки с вопросом
            val questionView = QuestionLayout(requireContext())
            // заполняем вьюшку из дата-объекта
            questionView.setData(q)
            // добавляем взаполненную вьюшку во фрагмент
            binding.questionContainer.addView(questionView)
        }

    }

    private fun getResultOfQuiz () : Int{
        var rightAnswersCount = 0
        for (q in QuizStorage.questions) {
            if (q.userAnswer.equals(q.rightAnswer)){
                rightAnswersCount += 1
            }
        }
        return rightAnswersCount
    }

    private fun clearAllRadioGroups() {
        val questContainer = binding.questionContainer
        for (i in 0 until questContainer.childCount) {
            val view = questContainer.getChildAt(i)
            // Проверяем, является ли текущая view объектом RadioGroup
            if (view is RadioGroup) {
                // Если да, сбрасываем выбор
                view.clearCheck()
            }
        }
    }

    private fun clearUserAnswers() {
        for (q in QuizStorage.questions) {
            q.userAnswer = ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
//    автоматически сгенерированный код - не нужен
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment FragmentQuestions.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            FragmentQuestions().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}