package tools.mo3ta.kgallery.ui.imagelist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tools.mo3ta.kgallery.data.ImagesRepoImpl
import tools.mo3ta.kgallery.data.ImagesService
import tools.mo3ta.kgallery.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ImageListingFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel = ImageListViewModel(ImagesRepoImpl(ImagesService.create()))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ImagesAdapter()

        binding.rvImages.adapter = adapter

        binding.edSearch.doAfterTextChanged {
            val data = viewModel.search(it.toString())
            adapter.update(data)
        }

        binding.ilSearch.setEndIconOnClickListener {
            adapter.update(viewModel.loadData())
        }

        lifecycleScope.launch {
            delay(2000)
            adapter.update(viewModel.loadData())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}