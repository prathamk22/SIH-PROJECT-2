package com.sih.project.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.sih.project.databinding.ActivityLoginBinding
import com.sih.project.model.User
import com.sih.project.ui.garbageCollectorUI.GarbageCollectorActivity
import com.sih.project.ui.home.HomeActivity
import com.sih.project.util.PreferenceHelper
import com.sih.project.util.Resource
import com.sih.project.util.UserTypes
import com.sih.project.viewmodel.MainViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PreferenceHelper.isLoggedIn) {
            if (UserTypes.valueOf(PreferenceHelper.userType) == UserTypes.USER) {
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                startActivity(Intent(this, GarbageCollectorActivity::class.java))
            }
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
                    when (UserTypes.valueOf(it.data.type ?: UserTypes.USER.name)) {
                        UserTypes.USER -> {
                            startActivity(Intent(this, HomeActivity::class.java))
                        }
                        UserTypes.COLLECTOR -> {
                            startActivity(Intent(this, GarbageCollectorActivity::class.java))
                        }
                    }
                    finish()
                }
                is Resource.Error -> {
                    binding.loginProgressBar.isVisible = false
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Idle -> {}
            }
        }
    }
}
