package com.sih.project.ui.home.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sih.project.R
import com.sih.project.databinding.FragmentAccountBinding
import com.sih.project.ui.LoginActivity
import com.sih.project.util.PreferenceHelper

class AccountFragment : Fragment() {

    private val viewModel: AccountViewModel by viewModels()

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.user.observe(viewLifecycleOwner) {
            binding.personName.text = it.name
            binding.personEmail.text = it.email
        }
        binding.logout.setOnClickListener {
            PreferenceHelper.userId = ""
            PreferenceHelper.isLoggedIn = false
            startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            requireActivity().finish()
        }
        binding.garbageHistory.setOnClickListener {
            findNavController().navigate(R.id.garbageFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
