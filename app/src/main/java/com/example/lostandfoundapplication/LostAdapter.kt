package com.example.lostandfoundapplication

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class LostAdapter(private val lostList: ArrayList<LostPost>) : RecyclerView.Adapter<LostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LostAdapter.ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.lost_list_items,
        parent, false)

        return ViewHolder(itemView)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val lostpost : LostPost = lostList[position]
        holder.location.text = lostpost.location
        holder.name.text = lostpost.name
        holder.phone.text = lostpost.phone
        holder.msg.text = lostpost.msg

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return lostList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val name: TextView = itemView.findViewById(R.id.name)
        val location: TextView = itemView.findViewById(R.id.location)
        val phone: TextView = itemView.findViewById(R.id.phone)
        val msg: TextView = itemView.findViewById(R.id.msg)
    }
}