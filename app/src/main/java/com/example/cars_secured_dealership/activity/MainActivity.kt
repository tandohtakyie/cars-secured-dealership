package com.example.cars_secured_dealership.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.cars_secured_dealership.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initNavigation()
    }

    private fun initNavigation(){
        val navController = findNavController(R.id.navHostFragment)

        NavigationUI.setupWithNavController(navView, navController)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener{_, destination, _ ->
            when(destination.id){
                R.id.homeFragment -> showBottomNavigation(true)
                R.id.totalsFragment -> showBottomNavigation(true)
                R.id.addCarDealFragment -> showBottomNavigation(true)
            }
        }
    }

    private fun showBottomNavigation(visible : Boolean){
        when(visible){
            true -> navView.visibility = View.VISIBLE
            false -> navView.visibility = View.GONE
        }
    }
}
