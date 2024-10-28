package com.paul.wallpaperapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.paul.wallpaperapp.MainActivity
import com.paul.wallpaperapp.R

class LoginActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var emailLoginEditText: EditText
    lateinit var passwordLoginEditText: EditText
    lateinit var btnSinInButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        firebaseAuth = FirebaseAuth.getInstance()
        emailLoginEditText = findViewById(R.id.etEmailAddress)
        passwordLoginEditText = findViewById(R.id.etPassword)
        btnSinInButton = findViewById(R.id.btnLogin)
        // one activity to signupPage
        val buttonSinUP: TextView = findViewById(R.id.signUp)
        buttonSinUP.setOnClickListener {
            // Create an Intent to start SecondActivity
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

        //
        btnSinInButton.setOnClickListener{
            login()
        }

    }

    private fun login(){
        val email = emailLoginEditText.text.toString()
        val password = passwordLoginEditText.text.toString()

        if (email.isBlank() || password.isBlank()){
            Toast.makeText(this, "Email and  Password can not blank", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){
            if(it.isSuccessful){
                Toast.makeText(this,"Login success", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this,"Not success", Toast.LENGTH_SHORT).show()


            }
        }

    }
}