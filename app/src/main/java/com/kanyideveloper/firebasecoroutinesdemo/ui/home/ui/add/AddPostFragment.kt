package com.kanyideveloper.firebasecoroutinesdemo.ui.home.ui.add

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kanyideveloper.firebasecoroutinesdemo.databinding.FragmentNotificationsBinding
import com.kanyideveloper.firebasecoroutinesdemo.util.Resource
import com.kanyideveloper.firebasecoroutinesdemo.util.showToast
import java.io.File

class AddPostFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddPostViewModel by viewModels()

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                latestTmpUri?.let {
                    binding.garbageImage.setImageURI(it)
                }
            }
        }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                latestTmpUri = it
                binding.garbageImage.setImageURI(uri)
            }
        }

    private var latestTmpUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addFromCamera.setOnClickListener { takeImage() }
        binding.addFromGallery.setOnClickListener { selectImageFromGalleryResult.launch("image/*") }

        binding.uploadButton.setOnClickListener {
            viewModel.uploadImage(binding.editTextComments.editText?.text?.toString(), latestTmpUri)
        }

        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    binding.progressBar.isVisible = false
                    requireContext().showToast(it.message)
                }
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                }
                is Resource.Idle -> {}
            }
            viewModel.setIdle()
        }

    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takePicture.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile =
            File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }

        return FileProvider.getUriForFile(
            requireActivity().applicationContext,
            "com.example.smartbins.provider",
            tmpFile
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
