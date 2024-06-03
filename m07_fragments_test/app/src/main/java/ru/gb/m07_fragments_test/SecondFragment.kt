package ru.gb.m07_fragments_test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import ru.gb.m07_fragments_test.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        val result = arguments?.getString("result")
        binding.textView.text = result

        binding.btnBack.setOnClickListener {
            // Этот метод удаляет все фрагменты из стека истории вплоть до первого. Таким образом,
            // вы возвращаетесь к главному фрагменту без сохранения истории текущего фрагмента.
            parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

//            findNavController().popBackStack() // выходит из приложения
        }

        return binding.root
    }
}