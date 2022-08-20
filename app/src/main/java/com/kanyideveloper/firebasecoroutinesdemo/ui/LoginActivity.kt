package com.kanyideveloper.firebasecoroutinesdemo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.kanyideveloper.firebasecoroutinesdemo.databinding.ActivityLoginBinding
import com.kanyideveloper.firebasecoroutinesdemo.ui.home.HomeActivity
import com.kanyideveloper.firebasecoroutinesdemo.util.PreferenceHelper
import com.kanyideveloper.firebasecoroutinesdemo.util.Resource
import com.kanyideveloper.firebasecoroutinesdemo.viewmodel.MainViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PreferenceHelper.isLoggedIn) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.buttonLogin.setOnClickListener {
            viewModel.signInUser(
                binding.editTextLoginEmail.editText?.text?.toString() ?: "",
                binding.editTextLoginPass.editText?.text?.toString() ?: ""
            )
        }

        binding.buttonRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        viewModel.userSignUpStatus.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    binding.loginProgressBar.isVisible = true
                }
                is Resource.Success -> {
                    binding.loginProgressBar.isVisible = false
                    Toast.makeText(applicationContext, "Logged In Successfully", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                is Resource.Error -> {
                    binding.loginProgressBar.isVisible = false
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
