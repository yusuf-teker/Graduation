package com.yt.graduation


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.yt.graduation.Authentication.LoginActivity
import com.yt.graduation.Settings.SettingsActivity
import com.yt.graduation.databinding.ActivityMainBinding


private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.mainToolBar)


        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.actionSignOut -> {
                    // Respond to navigation item 1 click
                    auth.signOut()
                    Toast.makeText(applicationContext, "Session has been closed", Toast.LENGTH_LONG).show()
                    goToLogin()
                    true
                }
                R.id.actionHomepage -> { //TODO
                    // Respond to navigation item 2 click
                    val navController = findNavController(R.id.fragmentContainerView)
                    navController.navigate(R.id.action_allProductsFragment_to_detailProductFragment)
                    true
                }
                R.id.actionSettings -> {
                    goToSettings()
                    true
                }
                R.id.actionChat -> { //TODO
                    true
                }
                else -> false
            }
        }

        auth = FirebaseAuth.getInstance()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        //menuInflater.inflate(R.menu.menu, menu)
        menuInflater.inflate(R.menu.bottom_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.actionSignOut) {
            auth.signOut()
            Toast.makeText(applicationContext, "Session has been closed", Toast.LENGTH_LONG).show()
            goToLogin()
        }
        if (item.itemId == R.id.actionHomepage) {
            val navController = findNavController(R.id.fragmentContainerView)
            navController.navigate(R.id.action_allProductsFragment_to_detailProductFragment)
        }
        if (item.itemId == R.id.actionSettings) {
            goToSettings()
        }
        return true
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun goToSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}