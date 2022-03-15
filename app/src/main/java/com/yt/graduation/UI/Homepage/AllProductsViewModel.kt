package com.yt.graduation.UI.Homepage


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.AuthResult
import com.yt.graduation.UI.Adapters.AllProductsAdapter
import com.yt.graduation.model.Product
import com.yt.graduation.repository.AllProductsRepository
import com.yt.graduation.util.Resource

class AllProductsViewModel: ViewModel() {

    private val repository = AllProductsRepository()

    private var _productList = MutableLiveData<ArrayList<Product>>()
    val productList: LiveData<ArrayList<Product>>
        get() = _productList


    fun refreshProducts(){
      repository.getProducts(object : AllProductsRepository.OnDataReceiveCallback{
          override fun onDataReceived(productList: ArrayList<Product>) {
              _productList.postValue(productList)
          }
      })
    }
}