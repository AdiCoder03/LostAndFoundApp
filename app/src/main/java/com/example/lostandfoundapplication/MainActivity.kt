package com.example.lostandfoundapplication

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val postLostBtn = findViewById<Button>(R.id.postLostBtnMain)
        val postFoundBtn = findViewById<Button>(R.id.postFoundBtnMain)
        val checkLostBtn = findViewById<Button>(R.id.checkLostBtnMain)
        val checkFoundBtn = findViewById<Button>(R.id.checkFoundBtnMain)
        val myPostsBtn = findViewById<Button>(R.id.myPostsBtnMain)
        val resetPasswdBtn = findViewById<Button>(R.id.changePasswdBtn)
        val profPic = findViewById<ImageView>(R.id.userProfPic)

        if(FirebaseAuth.getInstance().currentUser == null)
        {
            Toast.makeText(this, "Could not authenticate the user... Please log in again...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginPage :: class.java))
        }


        val storageReference = FirebaseStorage.getInstance().getReference("images/${FirebaseAuth.getInstance().currentUser!!.uid}")

        try {
            val tempImg = File.createTempFile("dp_temp_${Date().toString()}", ".jpg")
            storageReference.getFile(tempImg).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(tempImg.absolutePath)
                profPic.setImageBitmap(bitmap)
            }.addOnFailureListener{
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        catch (e : java.lang.Exception){
            Log.d("testing", e.toString())
        }

        postLostBtn.setOnClickListener {
            startActivity(Intent(this, PostForLostActivity :: class.java))
        }

        postFoundBtn.setOnClickListener {
            startActivity(Intent(this, PostForFoundActivity :: class.java))
        }

        checkLostBtn.setOnClickListener {
            startActivity(Intent(this, LostItemFeedActivity :: class.java))
        }

        checkFoundBtn.setOnClickListener {
            startActivity(Intent(this, FoundItemFeedActivity :: class.java))
        }

        myPostsBtn.setOnClickListener {
            startActivity(Intent(this, MyPostsActivity :: class.java))
        }

        resetPasswdBtn.setOnClickListener {
            startActivity(Intent(this, UpdatePasswordActivity :: class.java))
        }
    }
}