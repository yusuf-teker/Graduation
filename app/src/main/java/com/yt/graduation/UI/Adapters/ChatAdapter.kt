package com.yt.graduation.UI.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yt.graduation.R
import com.yt.graduation.model.Message


class ChatAdapter(private var messages: ArrayList<Message>): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val chatMessage: TextView = itemView.findViewById(R.id.message)
        val timeToSend: TextView = itemView.findViewById(R.id.timeToSend)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_chat_message, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMessage = messages[position]
        holder.chatMessage.text = currentMessage.messageText
        holder.timeToSend.text = currentMessage.timeToSend
    }

    override fun getItemCount(): Int {
        return messages.size
    }


    fun refreshMessages(messages : ArrayList<Message>){
        this.messages.clear()
        this.messages.addAll(messages)
        notifyDataSetChanged()
    }


}