package com.example.lostandfoundapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var userEmailField : TextView
    private lateinit var resetPasswdButton : Button
    private lateinit var authorizer : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        userEmailField = findViewById(R.id.inpUserEmailFPPage)
        resetPasswdButton = findViewById(R.id.resetPasswordButton)
        authorizer = FirebaseAuth.getInstance()

        resetPasswdButton.setOnClickListener {
            val userEmail = userEmailField.text.toString()
            authorizer.sendPasswordResetEmail(userEmail).addOnCompleteListener {
                if(it.isSuccessful)
                {
                    Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginPage :: class.java))
                }
                else
                {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}