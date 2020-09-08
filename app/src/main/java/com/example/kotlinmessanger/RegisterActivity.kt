package com.example.kotlinmessanger

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.widget.MediaController
import android.widget.Toast
import androidx.annotation.Nullable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.back_to_registration_textView_login
import kotlinx.android.synthetic.main.activity_main.email_editText_login
import kotlinx.android.synthetic.main.activity_main.login_button_login
import kotlinx.android.synthetic.main.activity_main.password_editText_login
import java.util.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkConnectivityStatus()

        login_button_login.setOnClickListener {
            performRegister()
        }

        back_to_registration_textView_login.setOnClickListener {
            Log.d("MainActivity","Try to show the Login Screen")

            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        selectphoto_name_register.setOnClickListener {
            Log.d("Main","Try to show photo")

            var intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)
        }

    }

    var selectedPhotoUri : Uri? =null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode== Activity.RESULT_OK && data != null){
            Log.d("main","Photo is selected")

            //var uri = data?.data
            selectedPhotoUri= data.data!!

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            val bitmapDrawable = BitmapDrawable(bitmap)
            selectphoto_name_register.setBackgroundDrawable(bitmapDrawable)
    }



    /*private fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        super.startActivityForResult(intent, requestCode, options)

        if(requestCode == 0){
            Log.d("main","Photo is selected")
            var uri = data?.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)

            val bitmapDrawable = BitmapDrawable(bitmap)
            selectphoto_name_register.setBackgroundDrawable(bitmapDrawable)
        }*/

    }

    private fun performRegister(){

        val email= email_editText_login.text.toString()
        val pwd = password_editText_login.text.toString()

        Log.d("MainActivity","user name is"+ pwd)
        Log.d("MainActivity","email is $email")

        if (pwd.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please Enter the text in email,pwd,username", Toast.LENGTH_SHORT).show()
            return }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pwd)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener


                Log.d("Main", "User Registered ${it.result!!.user!!.uid}")

                UploadImageToFirebaseStorage()

            }
            .addOnFailureListener {
                Log.d("Main","Failed to Create User {${it.message}}")
                Toast.makeText(this, "Failed to create user ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

   private fun UploadImageToFirebaseStorage(){

       if (selectedPhotoUri == null) return

       val filename=UUID.randomUUID().toString()

       val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

       ref.putFile(selectedPhotoUri!!)
           .addOnSuccessListener(){
               Log.d("register","Successfully upload image ${it.metadata!!.path}")

               ref.downloadUrl.addOnSuccessListener {
                   Log.d("main","Location Url is at $it")
               }

           }
   }

    private fun networkConnectivityStatus()
    {

        val a = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = a.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected)
        {
            if (networkInfo.type == ConnectivityManager.TYPE_MOBILE)
            {

                val tm =Toast.makeText(this, "Mobile Data is Connected", Toast.LENGTH_SHORT)
                tm.setGravity(Gravity.CENTER,0,0)
                tm.show()
            }
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI)
            {
                val t=Toast.makeText(this, "Wifi is Connected ", Toast.LENGTH_SHORT)
                t.setGravity(Gravity.CENTER,0,0)
                t.show()
            }
            else
            {
                val offline =  Toast.makeText(this, "You are Offline", Toast.LENGTH_SHORT)
                offline.setGravity(Gravity.CENTER,0,0)
                offline.show()
            }
        }

    }

}