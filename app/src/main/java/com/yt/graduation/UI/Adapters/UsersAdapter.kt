package com.yt.graduation.UI.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yt.graduation.R
import com.yt.graduation.UI.Account.AccountFragmentDirections
import com.yt.graduation.UI.Account.FavoriteProductsFragmentDirections
import com.yt.graduation.UI.Homepage.AllProductsFragmentDirections
import com.yt.graduation.UI.chat.UsersFragmentDirections
import com.yt.graduation.model.User

class UsersAdapter(private var users: ArrayList<User>,private val context: Context): RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val userName: TextView = itemView.findViewById(R.id.userName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_user, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser = users[position]
        if (currentUser.image.isEmpty() || currentUser.image == "default"){
            holder.userImage.setImageResource(R.drawable.defaultuser)
        }else{

        Glide.with(context)
            .load(currentUser.image) // image url
            .into(holder.userImage)

        }

        holder.userName.text = currentUser.name

        holder.itemView.setOnClickListener {
            val action = UsersFragmentDirections.actionUsersFragmentToChatFragment(currentUser)
            it.findNavController().navigate(action)

        }

    }

    fun refreshData(users: ArrayList<User>){
        this.users.clear()
        this.users = users
        notifyDataSetChanged()
    }

}