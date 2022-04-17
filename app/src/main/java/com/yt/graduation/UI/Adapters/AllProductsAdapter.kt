package com.yt.graduation.UI.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.yt.graduation.R
import com.yt.graduation.UI.Account.AccountFragmentDirections
import com.yt.graduation.UI.Account.FavoriteProductsFragmentDirections
import com.yt.graduation.UI.Homepage.AllProductsFragmentDirections
import com.yt.graduation.model.Product


open class AllProductsAdapter(private var products: ArrayList<Product>): RecyclerView.Adapter<AllProductsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : View.OnClickListener, RecyclerView.ViewHolder(itemView) {

        private lateinit var product: Product
        private val productImageView: ImageView = itemView.findViewById(R.id.productImage)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(product: Product) {
            this.product = product
            val context = itemView.context

            //
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Glide.with(context)
                .load(product.productImage) // image url
                .placeholder(circularProgressDrawable)
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
            if (it.findNavController().currentDestination?.id == R.id.allProductsFragment) {
                val action =
                    AllProductsFragmentDirections.actionAllProductsFragmentToDetailProductFragment(
                        currentItem
                    )
                it.findNavController().navigate(action)
            } else if (it.findNavController().currentDestination?.id == R.id.favoriteProductsFragment) {
                val action =
                    FavoriteProductsFragmentDirections.actionFavoriteProductsFragmentToDetailProductFragment(
                        currentItem
                    )
                it.findNavController().navigate(action)
            } else {
                val action = AccountFragmentDirections.actionAccountFragmentToDetailProductFragment(
                    currentItem
                )
                it.findNavController().navigate(action)
            }

        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_product, parent, false)

        return ViewHolder(itemView)
    }

    fun refreshData(products: ArrayList<Product>) {

        val diffCallback = ProductDiffCallback(this.products, products)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.products.clear()
        this.products.addAll(products)
        diffResult.dispatchUpdatesTo(this)
        //notifyDataSetChanged()
    }

    open class ProductDiffCallback(
        private val oldProducts: ArrayList<Product>,
        private val newProducts: ArrayList<Product>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldProducts.size
        }
        override fun getNewListSize(): Int {
            return newProducts.size
        }
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldProducts[oldItemPosition].productKey == newProducts[newItemPosition].productKey
        }
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldProducts[oldItemPosition] == newProducts[newItemPosition]
        }
    }

}