package ru.gb.m12_mvvm

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import ru.gb.m12_mvvm.databinding.ActivityMainBinding
import ru.gb.m12_mvvm.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }
        //binding = FragmentMainBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            val login = binding.login.text.toString()
            val password = binding.password.text.toString()
            viewModel.onSignInClick(login, password)
        }
        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.state
                    .collect{ state ->
                        when(state) {
                            State.Loading -> {
                                binding.progress.isVisible = true
                            }
                            State.Success -> {
                                binding.progress.isVisible = false
                            }
                        }
                    }
            }
    }
}








    // private val viewModel: MainViewModel by viewModels()
    // После такой ЛЕНИВОЙ инициализации (viewModel будет создан только тогда, когда мы к нему
    // обратимся первый раз) к viewModel можно обращаться из VIEW - mainActivity

    /* старая запись ==================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }
     */
