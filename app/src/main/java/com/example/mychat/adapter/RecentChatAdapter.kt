package com.example.mychat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mychat.R
import com.example.mychat.modal.RecentChats

class RecentChatAdapter : RecyclerView.Adapter<RecentChatHolder>() {

    private var listOfChats = mutableListOf<RecentChats>()
    private var listener: OnRecentChatClicked? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recentchatlist, parent, false)
        return RecentChatHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfChats.size
    }

    override fun onBindViewHolder(holder: RecentChatHolder, position: Int) {
        val recentChat = listOfChats[position]

        holder.userName.text = recentChat.name

        val shortenedMessage = recentChat.message?.take(10) ?: "Mesaj yok"
        val formattedMessage = "${recentChat.person}: $shortenedMessage"

        holder.lastMessage.text = formattedMessage

        Glide.with(holder.itemView.context)
            .load(recentChat.friendsimage)
            .into(holder.imageView)

        holder.timeView.text = recentChat.time?.substring(0, 5) ?: ""

        holder.itemView.setOnClickListener {
            listener?.getOnRecentChatClicked(position, recentChat)
        }
    }

    fun setOnRecentChatListener(listener: OnRecentChatClicked) {
        this.listener = listener
    }

    fun setOnRecentList(list: List<RecentChats>) {
        listOfChats.clear()
        listOfChats.addAll(list)
        notifyDataSetChanged()
     }
}


class RecentChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val userName: TextView = itemView.findViewById(R.id.recentChatTextName)
    val lastMessage: TextView = itemView.findViewById(R.id.recentChatTextLastMessage)
    val imageView: ImageView = itemView.findViewById(R.id.recentChatImageView)
    val timeView: TextView = itemView.findViewById(R.id.recentChatTextTime)
}


interface OnRecentChatClicked {
    fun getOnRecentChatClicked(position: Int, recentChat: RecentChats)
}

