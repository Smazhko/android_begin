package ru.gb.m12_mvvm

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
        // название биндинг класса = название класса со словами наоборот.
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

//
//        binding.btnSignIn.setOnClickListener {
//            val login = binding.login.text.toString()
//            val password = binding.password.text.toString()
//            viewModel.onSignInClick(login, password)
//        }

        viewLifecycleOwner.lifecycleScope
            .launch {
                viewModel.state
                    .collect{ state ->
                        when(state) {
                            is State.Loading -> {
                                binding.progress.isVisible = true
                                binding.btnSignIn.isEnabled = false
                                binding.loginInputLayout.error = null
                                binding.passwordInputLayout.error = null
                            }
                            is State.Success -> {
                                binding.progress.isVisible = false
                                binding.btnSignIn.isEnabled = true
                                binding.loginInputLayout.error = null
                                binding.passwordInputLayout.error = null
                            }
                            is State.Error -> {
                                binding.progress.isVisible = false
                                binding.btnSignIn.isEnabled = true
                                binding.loginInputLayout.error = state.loginError
                                binding.passwordInputLayout.error = state.passwordError
                            }
                        }
                    }
            }

        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.error
                    .collect { message ->
                        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
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
