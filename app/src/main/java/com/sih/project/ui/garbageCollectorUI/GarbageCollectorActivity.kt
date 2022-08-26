package com.sih.project.ui.garbageCollectorUI

import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sih.project.R
import com.sih.project.databinding.ActivityGarbageCollectorBinding
import com.sih.project.util.Utils


class GarbageCollectorActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityGarbageCollectorBinding

    private lateinit var viewModel: GarbageCollectorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[GarbageCollectorViewModel::class.java]
        binding = ActivityGarbageCollectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarGarbageCollector.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val headerView = navView.getHeaderView(0)
        val personTv = headerView.findViewById<TextView>(R.id.personName)
        val personEmail = headerView.findViewById<TextView>(R.id.personEmail)

        viewModel.user.observe(this) {
            personTv.text = it.name
            personEmail.text = it.email
        }

        val navController = findNavController(R.id.nav_host_fragment_content_garbage_collector)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_ongoing_trips,
                R.id.navigation_offers,
                R.id.nav_slideshow
            ),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_garbage_collector)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}