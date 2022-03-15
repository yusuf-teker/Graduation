package com.yt.graduation.UI.Homepage


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.yt.graduation.R
import com.yt.graduation.UI.Account.AccountActivity
import com.yt.graduation.UI.Authentication.LoginActivity
import com.yt.graduation.UI.Settings.SettingsActivity
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

                R.id.actionHomepage -> {
                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                    val navController = navHostFragment.navController
                    val currentFragment = navController.currentDestination?.id

                    if(currentFragment == R.id.detailProductFragment){
                        navController.navigate(DetailProductFragmentDirections.actionDetailProductFragmentToAllProductsFragment())
                    }
                    true
                }

                R.id.actionAccount -> {
                    goToAccount()
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
        menuInflater.inflate(R.menu.menu, menu)
        //menuInflater.inflate(R.menu.bottom_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.actionSettings) {
            goToSettings()
        }
        return true
    }

    private fun goToAccount() {
        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)
    }

    private fun goToSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun goToMain() {


        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_nav, AllProductsFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()


        //val intent = Intent(this, MainActivity::class.java)
        //startActivity(intent)

        /* TODO
        val currentFragment = supportFragmentManager.fragments.last()
        if (currentFragment is  DetailProductFragment ){
            val navController = Navigation.findNavController(
                this,
                R.id.main_nav
            )
            navController.navigate(R.id.action_detailProductFragment_to_allProductsFragment)
        }
        */
    }

}