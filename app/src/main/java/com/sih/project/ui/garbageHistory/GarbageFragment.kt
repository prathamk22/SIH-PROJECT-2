package com.sih.project.ui.garbageHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.sih.project.databinding.FragmentGarbageBinding
import com.sih.project.ui.home.ui.home.HomeRecyclerAdapter
import com.sih.project.util.Resource
import com.sih.project.util.showToast

class GarbageFragment : Fragment() {

    private val viewModel: GarbageViewModel by viewModels()

    private var _binding: FragmentGarbageBinding? = null
    private val binding get() = _binding!!

    private val homePostsRecyclerAdapter: HomeRecyclerAdapter by lazy {
        HomeRecyclerAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGarbageBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeRv.apply {
            adapter = homePostsRecyclerAdapter
        }
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    requireContext().showToast(it.message)
                    binding.loading.isVisible = false
                }
                is Resource.Idle -> {

                }
                is Resource.Loading -> {
                    binding.loading.isVisible = true
                }
                is Resource.Success -> {
                    binding.loading.isVisible = false
                    homePostsRecyclerAdapter.submitList(it.data)
                }
            }
        }
    }
}