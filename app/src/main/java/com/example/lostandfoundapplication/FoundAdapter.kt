package com.example.lostandfoundapplication

import android.graphics.BitmapFactory
import android.media.Image
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.FirebaseStorage.getInstance
import com.google.firebase.storage.StorageReference
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class FoundAdapter(private val foundList: ArrayList<FoundObjectPost>) : RecyclerView.Adapter<FoundAdapter.ViewHolder>() {

    private val storageReference = FirebaseStorage.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView =  LayoutInflater.from(parent.context).inflate(R.layout.found_list_items,
            parent, false)

        return ViewHolder(itemView)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foundPost : FoundObjectPost = foundList[position]
        holder.location.text = foundPost.location
        holder.name.text = foundPost.name
        holder.phone.text = foundPost.phone
        holder.msg.text = foundPost.msg
        holder.dateTime.text = foundPost.date_time
        if(foundPost.img_count!=null && foundPost.img_count > 0)
        {
            for(i in 0 until foundPost.img_count)
            {
                val tempFile = File.createTempFile("${foundPost.doc_id}_${i + 1}", ".jpg")
                storageReference.getReference("images/${foundPost.userID}_${foundPost.doc_id}_${i + 1}").getFile(tempFile).addOnSuccessListener{
                    val bitmap = BitmapFactory.decodeFile(tempFile.absolutePath)
                    holder.pics[i].setImageBitmap(bitmap)
                    holder.pics[i].visibility = View.VISIBLE
                }.addOnFailureListener{
                    Log.e("Image Retrieval failure", it.toString())
                }
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return foundList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val name: TextView = itemView.findViewById(R.id.name)
        val location: TextView = itemView.findViewById(R.id.location)
        val phone: TextView = itemView.findViewById(R.id.phone)
        val msg: TextView = itemView.findViewById(R.id.msg)
        val dateTime : TextView = itemView.findViewById(R.id.date_and_time)
        val pics = mutableListOf<ImageView>(itemView.findViewById(R.id.img1),itemView.findViewById(R.id.img2),itemView.findViewById(R.id.img3),itemView.findViewById(R.id.img4),itemView.findViewById(R.id.img5))
    }
}