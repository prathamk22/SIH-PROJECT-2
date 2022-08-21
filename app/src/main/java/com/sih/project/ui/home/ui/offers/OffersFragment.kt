package com.sih.project.ui.home.ui.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sih.project.databinding.FragmentDashboardBinding
import com.sih.project.util.Resource

class OffersFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val offersViewModel: OffersViewModel by viewModels()

    private val offersAdapter: OffersAdapter by lazy {
        OffersAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.offersRv.apply{
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = offersAdapter
        }
        offersViewModel.offers.observe(viewLifecycleOwner){
            when(it){
                is Resource.Error -> {
                    binding.loading.isVisible = false
                }
                is Resource.Idle -> {

                }
                is Resource.Loading -> {
                    binding.loading.isVisible = true
                }
                is Resource.Success -> {
                    binding.loading.isVisible = false
                    offersAdapter.submitList(it.data)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}