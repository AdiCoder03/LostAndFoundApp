package com.example.lostandfoundapplication

import android.graphics.BitmapFactory
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val type = intent.getStringExtra("postType")!!.toInt()
        val id = intent.getStringExtra("postID")
        val nameField = findViewById<TextView>(R.id.inpUserNameEditPostPage)
        val phone = findViewById<TextView>(R.id.inpUserPhoneEditPostPage)
        val location = findViewById<TextView>(R.id.inpWhereEditPostPage)
        val message = findViewById<TextView>(R.id.inpMessageEditPostPage)
        val updateBtn = findViewById<Button>(R.id.updateDetailsPostEditPage)
        val uploadImgBtn = findViewById<Button>(R.id.uploadImageButtonEditPostPage)
        val img = ArrayList<ImageView>()
        img.add(findViewById(R.id.picUpload1))
        img.add(findViewById(R.id.picUpload2))
        img.add(findViewById(R.id.picUpload3))
        img.add(findViewById(R.id.picUpload4))
        img.add(findViewById(R.id.picUpload5))
        val path : String
        if(type == 1)
        {
            path = "Lost Object Posts"
            location.hint = "Lost where"
        }
        else
        {
            path = "Found Object Posts"
            location.hint = "Found where"
        }

        var count = 0
        val imgURI = mutableListOf<Uri>()

        FirebaseFirestore.getInstance().collection(path).document(id!!).get().addOnSuccessListener {
            nameField.text = it.get("name").toString()
            phone.text = it.get("phone").toString()
            location.text = it.get("location").toString()
            message.text = it.get("msg").toString()
            count = it.get("img_count").toString().toInt()
            for(i in 0 until count)
            {
                val tempFile = File.createTempFile("${id}_${i + 1}", ".jpg")
                FirebaseStorage.getInstance().getReference("images/${it.get("userID").toString()}_${id}_${i + 1}").getFile(tempFile).addOnSuccessListener{
                    val bitmap = BitmapFactory.decodeFile(tempFile.absolutePath)
                    img[i].setImageBitmap(bitmap)
                    img[i].visibility = View.VISIBLE
                }.addOnFailureListener {
                    Log.e("Image Retrieval failure", it.toString())
                }
            }
        }

        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
            img[count].setImageURI(it)
            img[count].visibility = View.VISIBLE
            imgURI.add(it!!)
            Log.d("testing", it.toString())
            count++;
        }

        uploadImgBtn.setOnClickListener {
            if(count < 5) {
                getContent.launch("image/*")
            }
            else{
                Toast.makeText(this, "You can upload max 5 images", Toast.LENGTH_SHORT).show()
            }
        }

        updateBtn.setOnClickListener {
            FirebaseFirestore.getInstance().collection(path).document(id).update(
                mapOf(
                    "name" to nameField.text,
                    "phone" to phone.text,
                    "location" to location.text,
                    "msg" to message.text,
                    "img_count" to count
                )
            )

        }
    }
}