package com.yt.graduation.UI.Account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yt.graduation.model.Product
import com.yt.graduation.repository.FavoriteProductsRepository

class FavoriteProductsViewModel: ViewModel() {

    private val repository = FavoriteProductsRepository()

    private var _favoriteProducts = MutableLiveData<ArrayList<Product>>()
    val favoriteProducts: LiveData<ArrayList<Product>>
            get() = _favoriteProducts

    fun refreshData(){
        repository.refreshData(callback =object :FavoriteProductsRepository.OnDataReceiveCallback{
            override fun onDataReceived(productList: ArrayList<Product>) {
                _favoriteProducts.postValue(productList)
            }
        })
    }
}