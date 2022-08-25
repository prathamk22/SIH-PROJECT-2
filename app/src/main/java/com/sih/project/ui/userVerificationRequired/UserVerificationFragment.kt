package com.sih.project.ui.userVerificationRequired

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sih.project.databinding.FragmentUserVerificationBinding
import com.sih.project.model.UserPosts
import com.sih.project.util.Resource
import com.sih.project.util.showToast

class UserVerificationFragment : Fragment() {

    private val viewModel: UserVerificationViewModel by viewModels()
    private lateinit var binding: FragmentUserVerificationBinding

    private val userVerificationAdapter: UserVerificationAdapter by lazy {
        UserVerificationAdapter(::onVerificationClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userVerificationAdapter
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
                    userVerificationAdapter.submitList(it.data)
                }
            }
        }
    }

    fun onVerificationClicked(post: UserPosts) {

    }

}