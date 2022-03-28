package com.yt.graduation.UI.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yt.graduation.R
import com.yt.graduation.UI.Account.AccountFragmentDirections
import com.yt.graduation.UI.Account.FavoriteProductsFragmentDirections
import com.yt.graduation.UI.Homepage.AllProductsFragmentDirections
import com.yt.graduation.model.Product


class AllProductsAdapter(private var products: ArrayList<Product>): RecyclerView.Adapter<AllProductsAdapter.ViewHolder>() {
    class ViewHolder (itemView: View): View.OnClickListener, RecyclerView.ViewHolder(itemView){

        private lateinit var product: Product
        private val productImageView: ImageView = itemView.findViewById(R.id.productImage)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(product : Product){
            this.product = product
            val context = itemView.context
            Glide.with(context)
                .load(product.productImage) // image url
                .into(productImageView)
        }
        override fun onClick(p0: View?) {

        }

    }

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
    }

    override fun getItemCount(): Int {
        return  products.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_product, parent, false)

        return ViewHolder(itemView)
    }

    fun refreshData(products : ArrayList<Product>){
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }

}