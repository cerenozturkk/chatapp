package com.example.mychat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mychat.R
import com.example.mychat.modal.RecentChats

class RecentChatAdapter:RecyclerView.Adapter<RecentChatHolder>() {

    private var listofchats=listOf<RecentChats>()
    private var listener:onRecentChatClicked?=null
    private var recentModal=RecentChats()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentChatHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recentchatlist,parent,false)
        return RecentChatHolder(view)


    }

    override fun getItemCount(): Int {
        return listofchats.size

    }

    override fun onBindViewHolder(holder: RecentChatHolder, position: Int) {
        val recentchatlist=listofchats[position]

        recentModal=recentchatlist

        holder.userName.setText(recentchatlist.name)
        val themessage=recentchatlist.message!!




        holder.itemView.setOnClickListener{
            listener.getOnRecentChatClicked(position,recentchatlist)
        }



    }

    fun setOnRecentChatListener(listener:onRecentChatClicked){
        this.listener=listener


    }


}
class RecentChatHolder(itemview: View): RecyclerView.ViewHolder(itemview){



}
interface onRecentChatClicked{
    fun getOnRecentChatClicked(position:Int,recentchatlist:Recentchats)



}