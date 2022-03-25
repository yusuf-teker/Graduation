package com.yt.graduation.UI.Account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yt.graduation.model.Product
import com.yt.graduation.repository.OnSaleProductsRepository
import kotlinx.coroutines.launch

class OnSaleProductsViewModel : ViewModel() {

    private val repository = OnSaleProductsRepository()

    private var _productList = MutableLiveData<ArrayList<Product>>()
    val productList: LiveData<ArrayList<Product>>
        get() = _productList

    fun getOnSaleProducts(){
        viewModelScope.launch {
            repository.getProducts(object : OnSaleProductsRepository.OnDataReceiveCallback{
                override fun onDataReceived(productList: ArrayList<Product>) {
                    _productList.postValue(productList)
                }
            })
        }
    }
}