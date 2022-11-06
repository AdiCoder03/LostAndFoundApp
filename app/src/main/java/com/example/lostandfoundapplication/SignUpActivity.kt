package com.example.lostandfoundapplication

import android.content.Intent
import android.graphics.text.TextRunShaper
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.w3c.dom.Text

class SignUpActivity : AppCompatActivity() {

    private lateinit var nameField : TextView
    private lateinit var rollField : TextView
    private lateinit var emailField: TextView
    private lateinit var passwordField : TextView
    private lateinit var confirmPasswordField : TextView
    private lateinit var phoneField : TextView
    private lateinit var waField : TextView
    private lateinit var submitButton: Button
    private lateinit var authorizer : FirebaseAuth
    private lateinit var fDatabaseReference: DatabaseReference
    private lateinit var imgURI : Uri

    private fun isValidRoll(roll : String) : Boolean{
        return roll.isNotEmpty()
    }

    private fun isValidEmail(email : String) : Boolean {
        return email.isNotEmpty()
    }

    private fun isValidPasswd(passwd : String) : Boolean{
        return passwd.length > 6
    }

    private fun isValidPhone(phone : String) : Boolean{
        return phone.length == 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        nameField = findViewById(R.id.inpUserNameSUPage)
        rollField = findViewById(R.id.inpUserRollSUPage)
        emailField = findViewById(R.id.inpUserEmailSUPage)
        passwordField = findViewById(R.id.inpUserPasswdSUPage)
        confirmPasswordField = findViewById(R.id.confPasswdSUPage)
        phoneField = findViewById(R.id.inpUserPhoneNumber)
        waField = findViewById(R.id.inpUserWASUPage)
        submitButton = findViewById(R.id.submitDetailsButtonSUPage)
        authorizer = FirebaseAuth.getInstance()
        fDatabaseReference = FirebaseDatabase.getInstance().getReference("User Data")


        submitButton.setOnClickListener {
            val uName = nameField.text.toString()
            val uRoll = rollField.text.toString()
            val uEmail = emailField.text.toString()
            val uPasswd = passwordField.text.toString()
            val uConfPasswd = confirmPasswordField.text.toString()
            val uPhone = phoneField.text.toString()
            val uWA = waField.text.toString()

            val user = User(uName, uRoll, uPhone, uWA, uEmail)

            if(uName.isNotEmpty() &&
                isValidRoll(uRoll) &&
                    isValidEmail(uEmail) &&
                    isValidPasswd(uConfPasswd) &&
                    isValidPhone(uPhone) &&
                    isValidPhone(uWA) &&
                    uPasswd == uConfPasswd){
                authorizer.createUserWithEmailAndPassword(uEmail, uPasswd).addOnCompleteListener { it1 ->
                    if(it1.isSuccessful){
                        val uid = authorizer.currentUser?.uid
                        if(uid != null)
                        {
                            fDatabaseReference.child(uid).setValue(user).addOnCompleteListener { it2 ->
                                if(it2.isSuccessful)
                                {
                                    Toast.makeText(this, "User Registered successfully", Toast.LENGTH_SHORT).show()
                                    val fBaseUser : FirebaseUser? = authorizer.currentUser
                                    fBaseUser?.sendEmailVerification()?.addOnSuccessListener {
                                        Toast.makeText(this, "Verification mail sent... Please click on the verification link to proceed.", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, LoginPage :: class.java))
                                    }?.addOnFailureListener{
                                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                                    }
                                }
                                else
                                {
                                    Toast.makeText(this, it2.exception.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show()
                        }
                    }
                    if(!it1.isSuccessful){
                        Toast.makeText(this, it1.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}