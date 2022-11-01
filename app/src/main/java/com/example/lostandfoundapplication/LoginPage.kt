package com.example.lostandfoundapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.sign

class LoginPage : AppCompatActivity() {

    private lateinit var userEmailField : TextView
    private lateinit var userPasswdField : TextView
    private lateinit var loginPageButton : Button
    private lateinit var forgotPasswdButton : Button
    private lateinit var authorizer : FirebaseAuth
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        userEmailField = findViewById(R.id.inpUserEmailSIPage)
        userPasswdField = findViewById(R.id.inpPasswordSIPage)
        loginPageButton = findViewById(R.id.submitDetailsButtonSIPage)
        forgotPasswdButton = findViewById(R.id.forgotPasswordButtonSIPage)
        signUpButton = findViewById(R.id.signUpButton)
        authorizer = FirebaseAuth.getInstance()

        loginPageButton.setOnClickListener {
            val enteredEmail = userEmailField.text.toString()
            val enteredPassword = userPasswdField.text.toString()
            if(enteredEmail.isNotEmpty() && enteredPassword.isNotEmpty())
            {
                authorizer.signInWithEmailAndPassword(enteredEmail, enteredPassword).addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        if(authorizer.currentUser?.isEmailVerified == true) {
                            Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT)
                                .show()
                            val proceedIntent = Intent(this, MainActivity::class.java)
                            proceedIntent.putExtra("UID", authorizer.currentUser?.uid)
                            startActivity(proceedIntent)
                        }
                        else
                        {
                            Toast.makeText(this, "User not verified... Please verify the user through the email sent...", Toast.LENGTH_SHORT).show()
                            
                        }
                    }
                    else
                    {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        forgotPasswdButton.setOnClickListener {
            val forgotPasswdIntent = Intent(this, ForgotPasswordActivity :: class.java)
            startActivity(forgotPasswdIntent)
        }

        signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity :: class.java))
        }
    }
}