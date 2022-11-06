package com.example.lostandfoundapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PostForFoundActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_for_found)
        val userNameField = findViewById<TextView>(R.id.inpUserNameFoundPostPage)
        val userPhoneField = findViewById<TextView>(R.id.inpUserPhoneFoundPostPage)
        val locationField = findViewById<TextView>(R.id.inpWhereFoundFoundPostPage)
        val messageField = findViewById<TextView>(R.id.inpMessageFoundPostPage)
        val submitBtn = findViewById<TextView>(R.id.submitDetailsPostFoundPage)
        val fStore = FirebaseFirestore.getInstance()
        val docRefPostData = fStore.collection("Found Object Posts")
        val docRefUserData = fStore.collection("User Posts")

        submitBtn.setOnClickListener {
            val name = userNameField.text.toString()
            val phone = userPhoneField.text.toString()
            val location = locationField.text.toString()
            val message = messageField.text.toString()
            val user = FirebaseAuth.getInstance().currentUser
            var userInst = UserObj("dummy")

            if(user == null)
            {
                startActivity(Intent(this, LoginPage :: class.java))
                Toast.makeText(this, "Some error occurred... Please login again.", Toast.LENGTH_SHORT).show()
                finish()
            }
            else userInst = UserObj(user.uid)

            if(name.isNotEmpty()
                && phone.isNotEmpty()
                && location.isNotEmpty()
                && message.isNotEmpty())
            {
                docRefUserData.document(user!!.uid).set(userInst)
                val post = LostObjectPost (name, phone, location, message)
                docRefPostData.add(post).addOnSuccessListener { it1 ->
                    docRefUserData.document(user.uid).collection("Found Object Posts").add(it1.id).addOnFailureListener {
                        Log.d("testing", it.toString())
                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    }.addOnSuccessListener {
                        Toast.makeText(this, "Posted successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity :: class.java))
                        finish()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    Log.d("testing", it.toString())
                }
            }
            else
            {
                Toast.makeText(this, "Please fill all fields...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}