package ru.gb.m16_architecture.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.gb.m16_architecture.databinding.FragmentMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: FragmentMainBinding

//    @Inject
//    lateinit var mainViewModelFactory: MainViewModelFactory
//    private val viewModel: MainViewModel by viewModels { mainViewModelFactory }
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // вызываем загрузку данных при запуске программы

        viewModel.loadInitialHeroInfo()

        // Наблюдение за изменениями данных пользователя
        viewModel.heroLiveData.observe(viewLifecycleOwner) { hero ->
            hero?.let {
                // Загрузка изображения с помощью Glide
                Glide.with(this)
                    .load(hero.image)
//                    .placeholder(R.drawable.pngwing_com) // Изображение-заглушка
//                    .error(R.drawable.pngwing_com) // Изображение ошибки
                    .into(binding.img)
            }
        }

        // вводим наблюдателя за состоянием STATE из viewmodel.
        // в случае, если Fail - даём пользователю сообщение об ошибке
        // таким образом мы перенесли показ сообщения из ViewModel во View,
        // чтобы не передавать контекст в метод loadInitialUserInfo, так как
        // передача контекст фрагмента во viewmodel чревата утечками памяти.
        // Во вью модель можно безопасно передавать контекст приложения.
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                State.Fail -> {
                    Toast.makeText(requireContext(), "Ошибка в данных. Загрузка не удалась.", Toast.LENGTH_SHORT).show()
                }
                // и обработка других состояний, если понадобится
                else -> {}
            }
        }


    }

}