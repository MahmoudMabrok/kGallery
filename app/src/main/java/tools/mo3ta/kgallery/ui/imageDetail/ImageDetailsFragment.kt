package tools.mo3ta.kgallery.ui.imageDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tools.mo3ta.kgallery.R
import tools.mo3ta.kgallery.data.ImagesRepoImpl
import tools.mo3ta.kgallery.data.LocalImagesSourceImpl
import tools.mo3ta.kgallery.data.local.ImageLocalItem
import tools.mo3ta.kgallery.data.local.ImagesDB
import tools.mo3ta.kgallery.data.remote.ImagesService
import tools.mo3ta.kgallery.databinding.FragmentImageDetailBinding
import tools.mo3ta.kgallery.ui.imagelist.ImageListViewModel


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ImageDetailsFragment : Fragment() {

    private var _binding: FragmentImageDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args : ImageDetailsFragmentArgs by navArgs()

    private val imagesDao by lazy { ImagesDB.createDB(requireContext()).imagesDAO()}
    private val imageLocalSource by lazy { LocalImagesSourceImpl(imagesDao) }
    private val repo by lazy { ImagesRepoImpl(ImagesService.create() , imageLocalSource) }

    private val viewModel by lazy { ImageDetailViewModel(repo) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentImageDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchData(args.uri)

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    UiState.IDLE -> {}
                    is UiState.Success -> updateUI(state.data)
                    UiState.FinishSubmit -> findNavController().popBackStack()
                    is UiState.Resize -> {
                        delay(1000)
                        findNavController().popBackStack()
                    }
                }
            }
        }

        binding.btnSubmit.setOnClickListener {
            viewModel.submit(binding.edCaption.text.toString())
        }
    }

    private fun updateUI(item: ImageLocalItem) {
        binding.imageView.load(item.uri)
        binding.edCaption.setText(item.caption)
        binding.edCaption.error = if (item.caption.isEmpty()) getString(R.string.no_caption) else null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}