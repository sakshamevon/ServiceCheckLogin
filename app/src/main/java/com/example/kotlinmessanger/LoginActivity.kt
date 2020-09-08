package com.example.kotlinmessanger

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)    //this tells which activity to call after the oncreate method call

        login_button_login.setOnClickListener {

            val email = email_editText_login.text.toString()
            val pwd = password_editText_login.text.toString()

            Log.d("MainActivity","Attempt to login")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pwd)
                .addOnSuccessListener {

                }

        }

        back_to_registration_textView_login.setOnClickListener {

            finish()
        }

    }

}