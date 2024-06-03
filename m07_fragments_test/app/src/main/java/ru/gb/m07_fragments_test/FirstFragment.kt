package ru.gb.m07_fragments_test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.gb.m07_fragments_test.databinding.FragmentFirstBinding
import ru.gb.m07_fragments_test.databinding.FragmentSecondBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener {
            val randomNumber = (1..9).random()
            val result = randomNumber * randomNumber

            val bundle = Bundle()
            bundle.putString("result", "The square of $randomNumber is $result")

            val secondFragment = SecondFragment()
            secondFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, secondFragment)
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }
}