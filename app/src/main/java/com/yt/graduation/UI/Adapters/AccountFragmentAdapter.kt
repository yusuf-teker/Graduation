package com.yt.graduation.UI.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yt.graduation.UI.Account.FavoriteProductsFragment
import com.yt.graduation.UI.Account.OnSaleProductsFragment


class AccountFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            1 -> return OnSaleProductsFragment()
        }
        return FavoriteProductsFragment()
    }
}