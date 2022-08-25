package com.sih.project.ui.garbageCollectorUI.ui.onGoingTrips

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sih.project.databinding.FragmentOngoingTripBinding
import com.sih.project.model.CollectorTripEntity
import com.sih.project.util.Resource
import com.sih.project.util.showToast

class OnGoingTripsFragment : Fragment() {

    private var _binding: FragmentOngoingTripBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OnGoingTripsViewModel by viewModels()
    private val tripAdapter: TripAdapter by lazy {
        TripAdapter(::onUpdateStatus)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOngoingTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tripsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tripAdapter
        }
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    binding.progressBar.isVisible = false
                    requireContext().showToast(it.message)
                }
                is Resource.Idle -> {

                }
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    tripAdapter.submitList(it.data)
                }
            }
        }
    }

    private fun onUpdateStatus(selectedItem: String, item: CollectorTripEntity?) {
        viewModel.updateTripStatus(selectedItem, item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}