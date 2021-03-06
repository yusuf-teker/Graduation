package com.yt.graduation.UI.Adapters


import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yt.graduation.R
import com.yt.graduation.model.Message


class ChatAdapter(private var messages: ArrayList<Message>,private val receiverID: String, private val context: Context): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {


    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val chatMessage: TextView = itemView.findViewById(R.id.message)
        val timeToSend: TextView = itemView.findViewById(R.id.timeToSend)
        val messageRootLL: LinearLayout = itemView.findViewById(R.id.messageRootLL)
        val justifyRight: View = itemView.findViewById(R.id.justifyRight)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_chat_message, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val message = messages[position]
        holder.chatMessage.text = message.messageText
        holder.timeToSend.text = context.resources.getString(R.string.messageSendTime,message.timeToSend.subSequence(11,16),message.timeToSend.subSequence(5,7),message.timeToSend.subSequence(8,10))//LocalTime olarak geliyor


        /*  todo fix time text with a convenient way*/
        if (holder.chatMessage.text.length>25){
            holder.messageRootLL.orientation = LinearLayout.VERTICAL
        }else{
            holder.messageRootLL.orientation = LinearLayout.HORIZONTAL
        }

        if (message.senderId == receiverID){
            holder.messageRootLL.setBackgroundResource(R.drawable.message_background_receiver)
            holder.justifyRight.visibility = View.GONE
        }else{
            holder.messageRootLL.setBackgroundResource(R.drawable.message_background_sender)
            holder.justifyRight.visibility = View.VISIBLE
        }
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