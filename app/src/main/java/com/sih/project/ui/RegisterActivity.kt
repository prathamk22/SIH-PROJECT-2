package com.sih.project.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sih.project.databinding.ActivityRegisterBinding
import com.sih.project.ui.home.HomeActivity
import com.sih.project.util.Resource
import com.sih.project.viewmodel.MainViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.userRegisterButton.setOnClickListener {
            viewModel.createUser(
                binding.edxtUserName.editText?.text.toString(),
                binding.edxtEmailAddress.editText?.text.toString(),
                binding.edxtPhoneNum.editText?.text.toString(),
                binding.edxtPassword.editText?.text.toString()
            )
        }

        viewModel.userRegistrationStatus.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.registerProgress.isVisible = true
                }
                is Resource.Success -> {
                    binding.registerProgress.isVisible = false
                    Toast.makeText(
                        applicationContext,
                        "Registered Successfully",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    startActivity(Intent(this, HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                    finish()
                }
                is Resource.Error -> {
                    binding.registerProgress.isVisible = false
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}