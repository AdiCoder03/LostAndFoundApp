package com.example.lostandfoundapplication

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class MyPostAdapter(private val postList : ArrayList<MyPostObject>, private val context : MyPostsActivity) : RecyclerView.Adapter<MyPostAdapter.ViewHolder>(){
    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_posts_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post : MyPostObject = postList[position]
        val storageReference = FirebaseStorage.getInstance()
        holder.name.text = post.name
        holder.location.text = post.location
        holder.phone.text = post.phone
        holder.msg.text = post.msg
        holder.date.text = post.date_time
        var type = 0

        if(post.img_count!=null && post.img_count > 0)
        {
            for(i in 0 until post.img_count)
            {
                val tempFile = File.createTempFile("${post.doc_id}_${i + 1}", ".jpg")
                storageReference.getReference("images/${post.userID}_${post.doc_id}_${i + 1}").getFile(tempFile).addOnSuccessListener{
                    val bitmap = BitmapFactory.decodeFile(tempFile.absolutePath)
                    holder.pics[i].setImageBitmap(bitmap)
                    holder.pics[i].visibility = View.VISIBLE
                }.addOnFailureListener{
                    Log.e("Image Retrieval failure", it.toString())
                }
            }
        }
        val fs = FirebaseFirestore.getInstance()
        fs.collection("Lost Object Posts").document(post.doc_id!!).get().addOnSuccessListener {
            if(it.exists()) type = 1
            else type = 2
        }.addOnFailureListener {
            type = 2
        }
        holder.delBtn.setOnClickListener {
            if(type == 1)
                    fs.collection("Lost Object Posts").document(post.doc_id!!).delete()

            else
                    fs.collection("Found Object Posts").document(post.doc_id!!).delete()
        }
        holder.editBtn.setOnClickListener {
            val bundle = Bundle()
            val intent = Intent(context, EditActivity :: class.java)
            intent.putExtra("postID", post.doc_id)
            intent.putExtra("postType", type)
            startActivity(context, intent, bundle)
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.name)
        val phone : TextView = itemView.findViewById(R.id.phone)
        val msg : TextView = itemView.findViewById(R.id.msg)
        val location : TextView = itemView.findViewById(R.id.location)
        val date : TextView = itemView.findViewById(R.id.date_and_time)
        val delBtn : Button = itemView.findViewById(R.id.deleteBtn)
        val editBtn : Button = itemView.findViewById(R.id.editBtn)
        val pics = mutableListOf<ImageView>(itemView.findViewById(R.id.img1),itemView.findViewById(R.id.img2),itemView.findViewById(R.id.img3),itemView.findViewById(R.id.img4),itemView.findViewById(R.id.img5))
    }
}