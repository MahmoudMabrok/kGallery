package tools.mo3ta.kgallery.ui.imagelist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tools.mo3ta.kgallery.R
import tools.mo3ta.kgallery.data.ImagesRepoImpl
import tools.mo3ta.kgallery.data.LocalImagesSourceImpl
import tools.mo3ta.kgallery.data.local.ImagesDB
import tools.mo3ta.kgallery.data.remote.ImagesService
import tools.mo3ta.kgallery.databinding.FragmentListImagesBinding
import tools.mo3ta.kgallery.ui.imageDetail.ImageDetailsFragmentArgs

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ImageListingFragment : Fragment() {

    private var _binding: FragmentListImagesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val imagesDao by lazy { ImagesDB.createDB(requireContext()).imagesDAO()}
    private val imageLocalSource by lazy { LocalImagesSourceImpl(imagesDao)}
    private val repo by lazy { ImagesRepoImpl(ImagesService.create() , imageLocalSource)}

    private val viewModel by lazy { ImageListViewModel(repo) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ImagesAdapter{
            // open detail fragment
            findNavController().navigate( ImageListingFragmentDirections.actionImageListToDetails(it.uri))
        }

        binding.rvImages.adapter = adapter

        viewModel.fetchData()

        lifecycleScope.launch {
           viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.RESUMED) {
               viewModel.uiState.collect { state ->
                   binding.rvImages.isVisible = true
                   binding.ilSearch.isVisible = true
                   binding.spinLoading.isVisible = false
                   binding.tvNoData.isVisible = false
                   binding.tvNoSearchResults.isVisible = false

                   when (state) {
                       is UiState.Loading -> {
                           binding.spinLoading.isVisible = true
                       }

                       is UiState.Success -> {
                           adapter.update(state.data)
                       }

                       is UiState.NoData -> {
                           binding.tvNoData.isVisible = true
                       }

                       UiState.NotFound -> {
                           binding.rvImages.isVisible = false
                           binding.tvNoSearchResults.isVisible = true
                       }
                   }
               }
            }
        }

        binding.edSearch.doAfterTextChanged {
            viewModel.search(it.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}