package ru.gb.m17_recyclerview.presentation

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import ru.gb.m17_recyclerview.R
import ru.gb.m17_recyclerview.databinding.FragmentSinglePhotoBinding

class SinglePhotoFragment : Fragment() {
    private var _binding: FragmentSinglePhotoBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSinglePhotoBinding.inflate(inflater, container, false)

        val imageSrc = arguments?.getString("image_src")

        if (imageSrc != null) {
            Glide
                .with(binding.imgSinglePhoto.context)
                .load(imageSrc)
                .into(binding.imgSinglePhoto)
        }

        binding.btnBack.setOnClickListener {
            // Этот метод удаляет все фрагменты из стека истории вплоть до первого. Таким образом,
            // вы возвращаетесь к главному фрагменту без сохранения истории текущего фрагмента.
            parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        return binding.root
    }
}