package com.example.gonzch.firebaseexample.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.gonzch.firebaseexample.R
import com.example.gonzch.firebaseexample.data.model.FriendlyMessage

class MsgAdapter internal constructor(context: Context, objects:MutableList<FriendlyMessage>) : RecyclerView.Adapter<MessageViewHolder>(){

    var items: MutableList<FriendlyMessage>
    var itemsremoval : MutableList<FriendlyMessage>

    init {
        items = objects
        itemsremoval = items
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MessageViewHolder {
        return MessageViewHolder(parent)
    }

    override fun onBindViewHolder(holder: MessageViewHolder?, position: Int) {
        var objFriendly = items.get(position)
        val viewHolder = holder

        val isPhoto = objFriendly.photoUrl != null

        if (isPhoto){
            viewHolder!!.tv_message.visibility = View.GONE
            viewHolder.img.visibility = View.VISIBLE
            Glide.with(viewHolder.img.context)
                    .load(objFriendly.photoUrl)
                    .into(viewHolder.img)
        } else {
            viewHolder!!.tv_message.visibility = View.VISIBLE
            viewHolder.img.visibility = View.GONE
            viewHolder.tv_message.text = objFriendly.text
        }

        viewHolder.tv_user.text = objFriendly.name

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setMessage(message : FriendlyMessage){
        this.items.add(message)
    }

    fun removeAll(){
        items.removeAll(itemsremoval)
        notifyDataSetChanged()
    }
}

 class MessageViewHolder internal constructor(parent:ViewGroup?): RecyclerView.ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_message,parent,false)){
     var tv_user: TextView
     var tv_message: TextView
     var img: ImageView
     var mView: View

     init {
         tv_message = itemView.findViewById(R.id.messageTextView)
         tv_user = itemView.findViewById(R.id.nameTextView)
         img = itemView.findViewById(R.id.photoImageView)
         mView = itemView
     }
 }