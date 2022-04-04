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

    private var _signStatus = MutableLiveData<Boolean>()
    val signStatus: LiveData<Boolean>
        get() = _signStatus

    fun refreshProducts(){
        viewModelScope.launch {
            repository.getProducts(object : AllProductsRepository.OnDataReceiveCallback{
                override fun onDataReceived(productList: ArrayList<Product>) {
                    _productList.postValue(productList)
                }
            })
        }
    }

    fun isSigned(){
        _signStatus.postValue(repository.auth.currentUser!=null)
    }
}