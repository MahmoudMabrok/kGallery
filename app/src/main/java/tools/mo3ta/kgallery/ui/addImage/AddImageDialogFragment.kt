package tools.mo3ta.kgallery.ui.addImage

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.launch
import tools.mo3ta.kgallery.DIHelper
import tools.mo3ta.kgallery.databinding.DialogeAddImageBinding

class AddImageDialogFragment : DialogFragment() {

    private var mProfileUri:Uri? = null
    private var _binding: DialogeAddImageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        const val TAG = "AddImageDialogFragment"
        const val KEY_IMAGE_URI = "KEY_IMAGE_URI"
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                mProfileUri = fileUri

                binding.imageData.imageView.setImageURI(fileUri)
            }
        }

    private val repo by lazy { DIHelper.getInstance(requireContext()).repo }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogeAddImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOpenGallery.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)
                .galleryOnly()
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        binding.imageData.btnSubmit.setOnClickListener {
            mProfileUri?.let {uri ->
                val caption = binding.imageData.edCaption.text.toString()
                lifecycleScope.launch {
                    repo.addImage(uri.toString() , caption, true)
                    dismissAllowingStateLoss()
                }
            }
        }

        val uri = arguments?.getParcelable<Parcelable>(KEY_IMAGE_URI) as? Uri
        mProfileUri = uri
        mProfileUri?.let {
            binding.imageData.imageView.setImageURI(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}