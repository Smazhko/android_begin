package ru.gb.flagquiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import ru.gb.flagquiz.databinding.FragmentResultBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentResult.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentResult : Fragment() {
    // TODO: Rename and change types of parameters

    // чтобы параметры принимались этим фрагментом, надо их прописать в НАВИГАЦИИ
    //        <argument
    //            android:name="param1"
    //            app:argType="integer"
    //            android:defaultValue="0"
    //            />
    //        <argument
    //            android:name="param2"
    //            app:argType="integer"
    //            android:defaultValue="0"
    //            />
    private var param1: Int? = null
    private var param2: Int? = null

    private var _binding: FragmentResultBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // установка картинок для звёздочек в зависимости от количества правильных ответов
        // картники - цветная здвезда, либо серая. по умолчанию все - серые.
        // param1 автоматом принимает аргументы из фрагмента с вопросами,
        // потому что это задано в параметрах перехода от фрагмента с вопросами к фрагменту с результатами
        when (param1) {
            1 -> binding.imgStar1.setImageResource(R.drawable.star)
            2 -> {
                binding.imgStar1.setImageResource(R.drawable.star)
                binding.imgStar2.setImageResource(R.drawable.star)
            }
            3 -> {
                binding.imgStar1.setImageResource(R.drawable.star)
                binding.imgStar2.setImageResource(R.drawable.star)
                binding.imgStar3.setImageResource(R.drawable.star)
            }
        }

        // при прорисовке фрагмента - устанавливаем анимацию входа,
        // которая описана в res/anim/fragment_slide_in_up
        val slideInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fragment_slide_in_up)
        _binding?.root?.startAnimation(slideInAnimation)

        // при клике на кнопку - ЕЩЕ РАЗ - переходим на фрагмент с вопросами
        // чтобы не оставлять фрагмент в истории, чтобы к нему нельзя было вернуться по кнопке НАЗАД
        // в НАВИГАЦИИ прописываем
        //        app:popUpTo="@+id/ResultFrg"
        //        app:popUpToInclusive="true"
        binding.btnRepeat.setOnClickListener {
//            findNavController().popBackStack(R.id.ResultFrg, true) // ещё один способ, чтобы не сохранять фрагмент в истории
            // навигация перехода от фрагмента к фрагменту, прописанная в res/navigation
            findNavController().navigate(R.id.action_Result_to_Questions)
        }

        // установка начальных значений масштаба и прозрачности
        // для дальнейшего развёртывания анимации
        binding.imgStar1.scaleX = 0f
        binding.imgStar1.scaleY = 0f
        binding.imgStar1.alpha = 0f

        binding.imgStar2.scaleX = 0f
        binding.imgStar2.scaleY = 0f
        binding.imgStar2.alpha = 0f

        binding.imgStar3.scaleX = 0f
        binding.imgStar3.scaleY = 0f
        binding.imgStar3.alpha = 0f

        // анимация вылета звёздочек
        // установлена стартовая задержка setStartDelay(ххх), чтобы они вылетали по очереди
        binding.imgStar1.animate().apply {
            setStartDelay(500)
            scaleX(1f)
            scaleY(1f)
            rotation(360f)
            duration = 500
            alpha(1f)
            interpolator = AccelerateDecelerateInterpolator()
        }
        binding.imgStar2.animate().apply {
            setStartDelay(750)
            scaleX(1f)
            scaleY(1f)
            rotation(360f)
            duration = 500
            alpha(1f)
            interpolator = AccelerateDecelerateInterpolator()
        }
        binding.imgStar3.animate().apply {
            setStartDelay(1000)
            scaleX(1f)
            scaleY(1f)
            rotation(360f)
            duration = 500
            alpha(1f)
            interpolator = AccelerateDecelerateInterpolator()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentResult.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentResult().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}