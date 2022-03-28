package com.yt.graduation.UI.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.navigation.findNavController
import com.yt.graduation.R
import com.yt.graduation.UI.Account.AccountFragmentDirections
import com.yt.graduation.UI.Account.FavoriteProductsFragmentDirections
import com.yt.graduation.UI.Account.OnSaleProductsViewModel
import com.yt.graduation.UI.Homepage.AllProductsFragmentDirections
import com.yt.graduation.model.Product

//extended with AllProductAdapter
class OnSaleProductsAdapter(private var products: ArrayList<Product>, private val viewModel: OnSaleProductsViewModel): AllProductsAdapter(products) {

    //Override onBindViewHolder so We can delete items with long press
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = products[position]
        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            //Check current Destination because i use same adapter for both AllProducts and OnSaleProducts
            if (it.findNavController().currentDestination?.id == R.id.allProductsFragment){
                val action = AllProductsFragmentDirections.actionAllProductsFragmentToDetailProductFragment(currentItem)
                it.findNavController().navigate(action)
            }else if (it.findNavController().currentDestination?.id == R.id.favoriteProductsFragment){
                val action = FavoriteProductsFragmentDirections.actionFavoriteProductsFragmentToDetailProductFragment(currentItem)
                it.findNavController().navigate(action)
            }
            else{
                val action = AccountFragmentDirections.actionAccountFragmentToDetailProductFragment(currentItem)
                it.findNavController().navigate(action)
            }

        }
        holder.itemView.setOnLongClickListener(){
            showAddDeleteDialog(it.context,currentItem.productKey!!,currentItem.productName)

            true
        }



    }

    private fun showAddDeleteDialog(context: Context,productKey: String,productName: String) {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(context)
        dialog.setCancelable(false)
        dialog.setTitle("Delete $productName")
        dialog.setMessage("Are you sure you want to delete this item?" );
        dialog
            .setPositiveButton("Yes, delete it.") { dialog, id ->
                viewModel.deleteProduct(productKey)
                viewModel.refreshProducts()
            }
            .setNegativeButton("No, don't do that") { dialog, which ->

            }
        val alert: AlertDialog = dialog.create()
        alert.show()
    }


}