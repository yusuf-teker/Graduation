package com.yt.graduation.Account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.yt.graduation.MainActivity
import com.yt.graduation.R
import com.yt.graduation.UI.Adapters.AccountFragmentAdapter
import com.yt.graduation.databinding.ActivityAccountBinding
import com.yt.graduation.databinding.ActivitySettingsBinding

private lateinit var binding: ActivityAccountBinding
class AccountActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: AccountFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apply {
            tabLayout = accountTablayout
            viewPager = accountViewPager
        }

        adapter = AccountFragmentAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter
        tabLayout.addTab(tabLayout.newTab().setText("Favorites"))
        tabLayout.addTab(tabLayout.newTab().setText("On Sale"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //TODO("Not yet implemented")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                //TODO("Not yet implemented")
            }

        })
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                    tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.actionHomepage -> { //TODO
                    // Respond to navigation item 2 click
                    goToMain()
                    true
                }
                R.id.actionChat -> { //TODO
                    true
                }
                else -> false
            }
        }
        binding.fab.setOnClickListener{
            goToAddProduct()
        }



    }
    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    private fun goToAddProduct() {
        val intent = Intent(this, AddProductActivity::class.java)
        startActivity(intent)
    }
}


