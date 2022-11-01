package com.example.lostandfoundapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentUser = intent.getStringExtra("UID")
        val database = FirebaseDatabase.getInstance().getReference("User Data")
        val uidDisplayField = findViewById<TextView>(R.id.uidDisplay)
        uidDisplayField.text = currentUser

        val displayField = findViewById<TextView>(R.id.helloText)
        database.child(currentUser.toString()).get().addOnSuccessListener {
            if(it.exists()){
                displayField.text = it.child("name").value.toString()
            }
            else
            {
                Toast.makeText(this, "Couldn't find user...", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show()
        }
    }
}