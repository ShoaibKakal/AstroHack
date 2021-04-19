package com.kushalsharma.adastra

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout .activity_main)
         navView = findViewById(R.id.nav_view)

//        val navController = findNavController(R.id.nav_host_fragment)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.navigation_home, R.id.navigation_Music, R.id.navigation_profile))
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)

        setUpNavigation()
        supportActionBar?.hide()
        navView.itemIconTintList = null

    }

    private fun setUpNavigation() {

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        NavigationUI.setupWithNavController(
            navView,
            navHostFragment!!.navController
        )
    }


    override fun onBackPressed() {

        if (Navigation.findNavController(this, R.id.nav_host_fragment)
                .getCurrentDestination()!!.getId() == R.id.navigation_socialize
        ) {
            // handle back button the way you want here
            finish()
        }
        super.onBackPressed()
    }
}