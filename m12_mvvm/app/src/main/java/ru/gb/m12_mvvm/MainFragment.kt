package ru.gb.m12_mvvm

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
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
        binding.search.addTextChangedListener {it ->
            if (it.isNullOrEmpty() || it.length < 3)
                binding.button.isEnabled = false
            else
                binding.button.isEnabled = true
        }

        binding.button.setOnClickListener {
            val searchString = binding.search.text.toString()
            viewModel.onSignInClick(searchString)
        }

        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.state
                    .collect{ state ->
                        when(state) {
                            State.Start -> {
                                binding.search.isEnabled = true
                                binding.button.isEnabled = false
                                binding.searchResult.text = getString(R.string.search_result)
                                binding.progress.isVisible = false

                            }
                            State.Loading -> {
                                binding.search.isEnabled = false
                                binding.button.isEnabled = false
                                binding.searchResult.text = getString(R.string.search_continue)
                                binding.progress.isVisible = true
                            }
                            State.Success -> {
                                binding.search.isEnabled = true
                                binding.button.isEnabled = true
                                binding.searchResult.text = getString(R.string.search_no_result)
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
