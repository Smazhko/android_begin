package ru.gb.m17_recyclerview.presentation

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.gb.m17_recyclerview.R
import ru.gb.m17_recyclerview.data.PhotoDTO
import ru.gb.m17_recyclerview.databinding.FragmentPhotoListBinding

@AndroidEntryPoint
class PhotoListFragment : Fragment() {
    private var _binding: FragmentPhotoListBinding? = null
    private val binding get () = _binding!!

    private val viewModel: PhotoListViewModel by viewModels()

    private val customAdapter = CustomAdapter{photo -> onItemClick(photo)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = customAdapter

        viewModel.photosStateFlow.onEach {
            customAdapter.setData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onItemClick(item: PhotoDTO){
        val bundle = Bundle()
        bundle.putString("image_src", item.imgSrcHttps)

        val secondFragment = SinglePhotoFragment()
        secondFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, secondFragment)
            .addToBackStack(null)
            .commit()
    }
}