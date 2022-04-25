package com.yt.graduation.main


import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.yt.graduation.R
import com.yt.graduation.databinding.ActivityMainBinding
import android.view.MenuInflater

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController:NavController
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.isSigned()
        setSupportActionBar(binding.mainToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.mainToolBar.setNavigationOnClickListener { onBackPressed() }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.bottomNavigation.setupWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.allProductsFragment,R.id.usersFragment, R.id.accountFragment,R.id.loginFragment,R.id.registerFragment))


        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            viewModel.isSigned()
            if(destination.id == R.id.loginFragment || destination.id == R.id.registerFragment || destination.id == R.id.chatFragment || destination.id == R.id.wallpaperFragment) {
                binding.bottomNavigation.visibility = View.GONE
            } else {

                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }




    }


    override fun onSupportNavigateUp(): Boolean { //UP Button
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.actionSettings) {
            findNavController(R.id.fragmentContainerView).navigate(R.id.settingsFragment)
        }
        else if (item.itemId == R.id.actionChangeWallpaper) {
            findNavController(R.id.fragmentContainerView).navigate(R.id.wallpaperFragment)
        }
        return true
    }

}