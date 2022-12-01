package com.example.lostandfoundapplication

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import papaya.`in`.sendmail.SendMail
import java.io.File
import kotlin.collections.ArrayList

class LostAdapter(private val lostList: ArrayList<LostObjectPost>) : RecyclerView.Adapter<LostAdapter.ViewHolder>() {

    private val storageReference = FirebaseStorage.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView =  LayoutInflater.from(parent.context).inflate(R.layout.lost_list_items,
        parent, false)

        return ViewHolder(itemView)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lostPost : LostObjectPost = lostList[position]
        holder.location.text = lostPost.location
        holder.name.text = lostPost.name
        holder.phone.text = lostPost.phone
        holder.msg.text = lostPost.msg
        holder.dateTime.text = lostPost.date_time
        if(lostPost.img_count!=null && lostPost.img_count > 0)
        {
            for(i in 0 until lostPost.img_count)
            {
                val tempFile = File.createTempFile("${lostPost.doc_id}_${i + 1}", ".jpg")
                storageReference.getReference("images/${lostPost.userID}_${lostPost.doc_id}_${i + 1}").getFile(tempFile).addOnSuccessListener{
                    val bitmap = BitmapFactory.decodeFile(tempFile.absolutePath)
                    holder.pics[i].setImageBitmap(bitmap)
                    holder.pics[i].visibility = View.VISIBLE
                }.addOnFailureListener{
                    Log.e("Image Retrieval failure", it.toString())
                }
            }
        }
        holder.foundBtn.setOnClickListener {
            FirebaseFirestore.getInstance().collection("User Data").document(FirebaseAuth.getInstance().currentUser!!.uid).get().addOnSuccessListener {it1->
                FirebaseFirestore.getInstance().collection("User Data").document(lostPost.userID!!).get().addOnSuccessListener {
                    val message =
                        "You had posted for a lost object which has been reported found by a user whose contact details are as follows:\nEmail: ${it1.get("email")}\nPhone: ${it1.get("phone")}"
                    val mail: SendMail = SendMail(
                        "lostandfoundappiitp@gmail.com",
                        "ghvaflrlunstjrwd",
                        it.get("email").toString(),
                        "Lost object reported found",
                        message
                    )
                    mail.execute()
                }
            }
        }
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
        val dateTime : TextView = itemView.findViewById(R.id.date_and_time)
        val foundBtn : Button = itemView.findViewById(R.id.claimBtn)
        val pics = mutableListOf<ImageView>(itemView.findViewById(R.id.img1),itemView.findViewById(R.id.img2),itemView.findViewById(R.id.img3),itemView.findViewById(R.id.img4),itemView.findViewById(R.id.img5))
    }
}