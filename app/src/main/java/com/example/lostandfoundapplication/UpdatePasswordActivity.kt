package com.example.lostandfoundapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text

class UpdatePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)
        val oldPasswordField = findViewById<TextView>(R.id.inpCurrPasswdUPPage)
        val newPasswordField = findViewById<TextView>(R.id.inpNewPasswdUPPage)
        val confPasswordField = findViewById<TextView>(R.id.inpConfNewPasswdUPPage)
        val submitButton = findViewById<Button>(R.id.submitButtonUPPage)
        var userEmail : String
        FirebaseDatabase.getInstance().getReference("User Data").get().addOnSuccessListener()
        {
            userEmail = it.child("email").value.toString()
        }.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
        submitButton.setOnClickListener {
            val oldPasswd = oldPasswordField.text.toString()
            val newPasswd = newPasswordField.text.toString()
            val confPasswd = confPasswordField.text.toString()
            if(oldPasswd.isNotEmpty()
                && newPasswd.isNotEmpty()
                && confPasswd.isNotEmpty())
            {
                if(newPasswd == confPasswd)
                {
                    val user = FirebaseAuth.getInstance().currentUser
                    if(user != null && user.email != null)
                    {
                        val credential = EmailAuthProvider.getCredential(user.email!!, oldPasswd)
                        user.reauthenticate(credential)
                            .addOnCompleteListener { it1 ->
                                if(it1.isSuccessful)
                                {
                                    user.updatePassword(newPasswd).addOnCompleteListener {
                                        if(it.isSuccessful)
                                        {
                                            Toast.makeText(this, "Password reset successful... Please login again with the new password...", Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this, LoginPage :: class.java))
                                            finish()
                                        }
                                        else
                                        {
                                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                                else
                                {
                                    Toast.makeText(this, it1.exception.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                    else
                    {
                        Toast.makeText(this, "Re-authentication failed... Please login again...", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginPage :: class.java))
                        finish()
                    }
                }
                else
                {
                    Toast.makeText(this, "New password and confirmation password should be same...", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(this, "Please fill all fields before proceeding...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}