package tools.mo3ta.kgallery.ui.imageDetail

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
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
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tools.mo3ta.kgallery.DIHelper
import tools.mo3ta.kgallery.R
import tools.mo3ta.kgallery.data.local.ImageLocalItem
import tools.mo3ta.kgallery.databinding.FragmentImageDetailBinding
import tools.mo3ta.kgallery.utils.Utils
import tools.mo3ta.kgallery.worker.ResizeWorker
import tools.mo3ta.kgallery.worker.UpdateCaptionWorker


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ImageDetailsFragment : Fragment() {

    private var _binding: FragmentImageDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args : ImageDetailsFragmentArgs by navArgs()

    private val repo by lazy { DIHelper.getInstance(requireContext()).repo }

    private val viewModel by lazy { ImageDetailViewModel(repo) }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            try {
                doResizing(requireContext())
            } catch (_: Exception) {

            }
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
                        onResizeState()
                    }

                    is UiState.ScheduleUpdateCaption -> {
                        scheduleUpdate(requireContext() , state.data , state.caption)
                    }
                }
            }
        }

        binding.imageData.btnSubmit.setOnClickListener {
            viewModel.submit(
                Utils.isNetworkConnected(requireContext()),
                binding.imageData.edCaption.text.toString(),
                binding.imageData.edWidth.text.toString().toIntOrNull() ?: 0 ,
                binding.imageData.edHeight.text.toString().toIntOrNull() ?: 0)
        }
    }

    private fun scheduleUpdate(context: Context, data: ImageLocalItem, caption: String) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val myWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<UpdateCaptionWorker>()
                .setConstraints(constraints)
                .setInputData(UpdateCaptionWorker.createInputData(data.uri , caption))
                .build()

        WorkManager.getInstance(context).enqueue(myWorkRequest)

        findNavController().popBackStack()
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

        findNavController().popBackStack()
    }

    private fun updateUI(item: ImageLocalItem) {

        if (item.isAppGallery){
            binding.imageData.imageView.setImageURI(Uri.parse(item.uri))
        }else{
            val imageLoader = requireContext().imageLoader
            val request = ImageRequest.Builder(requireContext())
                .data(item.uri)
                .allowHardware(false)
                .listener (onSuccess = { _: ImageRequest, successResult: SuccessResult ->
                    val drawable = successResult.drawable
                    val width = drawable.intrinsicWidth
                    val height = drawable.intrinsicHeight
                    viewModel.updateSize(width, height)
                    binding.imageData.edWidth.setText(width.toString())
                    binding.imageData.edHeight.setText(height.toString())
                    binding.imageData.imageView.setImageDrawable(drawable)
                    lifecycleScope.launch(Dispatchers.IO) {
                        val color = Utils.getDominantColor(drawable)
                        withContext(Dispatchers.Main){
                            binding.pageContainer.setBackgroundColor(color)
                        }
                    }
                })
                .build()

            imageLoader.enqueue(request)
        }

        binding.imageData.edCaption.setText(item.caption)
        binding.imageData.edCaption.error = if (item.caption.isEmpty()) getString(R.string.no_caption) else null
        binding.imageData.imageView.load(item.uri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}