package com.sih.project.ui.home.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sih.project.databinding.FragmentHomeBinding
import com.sih.project.util.Resource
import com.sih.project.util.showToast

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private val homePostsRecyclerAdapter: HomeRecyclerAdapter by lazy {
        HomeRecyclerAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}