package tools.mo3ta.kgallery.ui.imageDetail

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkRequest
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
import tools.mo3ta.kgallery.worker.ResizeWorker


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


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            doResizing(requireContext())
        }


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

                        onResizeState()
                    }
                }
            }
        }

        binding.btnSubmit.setOnClickListener {
            viewModel.submit(binding.edCaption.text.toString(),
                width = binding.edWidth.text.toString().toIntOrNull() ?: 0 ,
                height = binding.edHeight.text.toString().toIntOrNull() ?: 0)
        }
    }

    private fun onResizeState(){
        Log.d("TestTest", "onResizeState: ")

        // ask for permission to show notifications on android 13+
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
            || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            doResizing(requireContext())
        } else {
            requestPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }


    private fun doResizing(context: Context) {
        Log.d("TestTest", "doResizing: ")
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val myWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<ResizeWorker>()
                .setConstraints(constraints)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()

        WorkManager.getInstance(context).enqueue(myWorkRequest)

        WorkManager.getInstance(context).getWorkInfoByIdLiveData(myWorkRequest.id).observe(viewLifecycleOwner){
            Log.d("TestTest", "getWorkInfoByIdLiveData: $it")
            if (it.state.isFinished){
                Log.d("TestTest", "doResizing: isFinished")
            }else{
                Log.d("TestTest", "doResizing: state${it.state.name}")
            }
        }

        findNavController().popBackStack()
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