package com.yt.graduation.UI.Homepage


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yt.graduation.model.Product
import com.yt.graduation.repository.AllProductsRepository
import kotlinx.coroutines.launch


class AllProductsViewModel: ViewModel() {

    private val repository = AllProductsRepository()

    private var _productList = MutableLiveData<ArrayList<Product>>()
    val productList: LiveData<ArrayList<Product>>
        get() = _productList

    //View Model View Model arası
    fun setProductList(products: ArrayList<Product>){
        _productList.postValue(products)
    }

    fun refreshProducts(){
        viewModelScope.launch {
            repository.getProducts(object : AllProductsRepository.OnDataReceiveCallback{
                override fun onDataReceived(productList: ArrayList<Product>) {
                    _productList.postValue(productList)
                }
            })
        }

    }
}