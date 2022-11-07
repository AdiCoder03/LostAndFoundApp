package com.example.lostandfoundapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

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
        val picUpload = mutableListOf<ImageView>()
        picUpload.add(findViewById<ImageView>(R.id.picUpload1))
        picUpload.add(findViewById<ImageView>(R.id.picUpload2))
        picUpload.add(findViewById<ImageView>(R.id.picUpload3))
        picUpload.add(findViewById<ImageView>(R.id.picUpload4))
        picUpload.add(findViewById<ImageView>(R.id.picUpload5))
        val uploadImageBtn = findViewById<Button>(R.id.uploadImageButtonLostPostPage)
        val imgURI = mutableListOf<Uri>()


        val user = FirebaseAuth.getInstance().currentUser

        if(user == null)
        {
            startActivity(Intent(this, LoginPage :: class.java))
            Toast.makeText(this, "Some error occurred... Please login again.", Toast.LENGTH_SHORT).show()
            finish()
        }

        fStore.collection("User Data").document(user!!.uid).get().addOnSuccessListener {
            userNameField.text = it.get("name").toString()
            userPhoneField.text = it.get("phone").toString()
        }
        var count = 0;

        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
            picUpload[count].setImageURI(it)
            picUpload[count].visibility = View.VISIBLE
            imgURI.add(it!!)
            Log.d("testing", it.toString())
            count++;
        }

        uploadImageBtn.setOnClickListener {
            if(count < 5) {
                getContent.launch("image/*")
            }
            else{
                Toast.makeText(this, "You can upload max 5 images", Toast.LENGTH_SHORT).show()
            }
        }
//        uploadImageBtn.setOnClickListener {
//            var imgURI : Uri
//            val storageReference = FirebaseStorage.getInstance().getReference("images/$")
//            storageReference.putFile(imgURI).addOnCompleteListener{
//                if(it.isSuccessful) {
//                    Log.d("testing", "Photo uploaded successfully")
//                }
//                else
//                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
//            }
//        }


        submitBtn.setOnClickListener {
            val name = userNameField.text.toString()
            val phone = userPhoneField.text.toString()
            val location = locationField.text.toString()
            val message = messageField.text.toString()
            val imageList = mutableListOf<String>()

            docRefUserData = FirebaseFirestore.getInstance().collection("User Data").document(user.uid).collection("Lost")


            if(name.isNotEmpty()
                && phone.isNotEmpty()
                && location.isNotEmpty()
                && message.isNotEmpty())
            {
                val post = LostObjectPost (name, phone, location, message, user.uid, count)
                docRefPostData.add(post).addOnSuccessListener { it1 ->
                    docRefUserData.add(PostObj(it1.id)).addOnSuccessListener {

                        if(count > 0)
                        {
                            var allOk = true
                            val storageReference = FirebaseStorage.getInstance()
                            for(i in 0 until count)
                            {
                                Log.d("testing 1", "$i")
                                val filePath = ("images/${user.uid}_${it1.id}_${i + 1}")
                                storageReference.getReference(filePath).putFile(imgURI[i])
                                imageList.add(filePath)
                            }
                            for(i in 0 until count)
                            {
                                docRefPostData.document(it1.id).collection("images").add(ImageURLObj(imageList[i]))
                            }
                            Toast.makeText(this, "Posted successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity :: class.java))
                            finish()
                        }
                        else {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
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