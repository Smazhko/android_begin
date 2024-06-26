package ru.gb.flagquiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.txtResult.text = "$param1 / $param2"

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

        binding.btnRepeat.setOnClickListener {
//            findNavController().popBackStack(R.id.ResultFrg, true)
            findNavController().navigate(R.id.action_Result_to_Questions)
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