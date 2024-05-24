package ru.gb.m14_retrofit

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import ru.gb.m14_retrofit.databinding.FragmentUserInfoBinding

class UserInfoFragment : Fragment() {

    companion object {
        fun newInstance() = UserInfoFragment()
    }

    private lateinit var binding: FragmentUserInfoBinding
    private val viewModel: UserInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // вызываем загрузку данных при запуске программы

        viewModel.loadInitialUserInfo()

        // Наблюдение за изменениями данных пользователя
        viewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            user?.let {
                // Загрузка изображения с помощью Glide
                Glide.with(this)
                    .load(user.picture.large)
//                    .placeholder(R.drawable.pngwing_com) // Изображение-заглушка
//                    .error(R.drawable.pngwing_com) // Изображение ошибки
                    .into(binding.imgPhoto)
            }
        }

        // вводим наблюдателя за состоянием STATE из viewmodel.
        // в случае, если Fail - даём пользователю сообщение об ошибке
        // таким образом мы перенесли показ сообщения из ViewModel во View,
        // чтобы не передавать контекст в метод loadInitialUserInfo, так как
        // передача контекст фрагмента во viewmodel чревата утечками памяти.
        // Во вью модель можно безопасно передавать контекст приложения.
        viewModel.state.observe(viewLifecycleOwner) { state ->
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
