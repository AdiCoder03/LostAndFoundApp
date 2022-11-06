package com.example.lostandfoundapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class PostForLostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_for_lost)
        val userNameField = findViewById<TextView>(R.id.inpUserNameLostPostPage)
        val userPhoneField = findViewById<TextView>(R.id.inpUserPhoneLostPostPage)
        val locationField = findViewById<TextView>(R.id.inpWhereLostLostPostPage)
        val messageField = findViewById<TextView>(R.id.inpMessageLostPostPage)
        val submitBtn = findViewById<TextView>(R.id.submitDetailsPostLostPage)
        val fStore = FirebaseFirestore.getInstance()
        val docRefPostData = fStore.collection("Lost Object Posts")
        var docRefUserData : CollectionReference

        submitBtn.setOnClickListener {
            val name = userNameField.text.toString()
            val phone = userPhoneField.text.toString()
            val location = locationField.text.toString()
            val message = messageField.text.toString()
            val user = FirebaseAuth.getInstance().currentUser

            if(user == null)
            {
                startActivity(Intent(this, LoginPage :: class.java))
                Toast.makeText(this, "Some error occurred... Please login again.", Toast.LENGTH_SHORT).show()
                finish()
            }

            docRefUserData = FirebaseFirestore.getInstance().collection("User Data").document(user!!.uid).collection("Lost")


            if(name.isNotEmpty()
                && phone.isNotEmpty()
                && location.isNotEmpty()
                && message.isNotEmpty())
            {
                val post = LostObjectPost (name, phone, location, message)
                docRefPostData.add(post).addOnSuccessListener { it1 ->
                    docRefUserData.add(PostObj(it1.id)).addOnSuccessListener {
                        Toast.makeText(this, "Posted successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity :: class.java))
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(this, "Please fill all fields...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}