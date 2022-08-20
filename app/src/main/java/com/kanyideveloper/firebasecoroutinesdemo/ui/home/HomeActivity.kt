package com.kanyideveloper.firebasecoroutinesdemo.ui.home

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.kanyideveloper.firebasecoroutinesdemo.R
import com.kanyideveloper.firebasecoroutinesdemo.databinding.ActivityHome2Binding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHome2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHome2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity_home2)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home,
                R.id.navigation_offers,
                R.id.navigation_add,
                R.id.navigation_wallet,
                R.id.navigation_account -> {
                    binding.toolbar.isVisible = true
                    binding.navView.isVisible = true
                }
                else -> {
                    binding.toolbar.isVisible = false
                    binding.navView.isVisible = false
                }
            }
        }
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_offers,
                R.id.navigation_add,
                R.id.navigation_wallet,
                R.id.navigation_account
            )
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }
}