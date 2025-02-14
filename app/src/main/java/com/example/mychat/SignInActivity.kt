package com.example.mychat
import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.mychat.databinding.ActivitySignInBinding
import androidx.databinding.DataBindingUtil
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class SignInActivity : AppCompatActivity() {


    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: AlertDialog
    private lateinit var signInBinding:ActivitySignInBinding


    private fun signIn(password: String, email: String) {
        progressDialog.show()
        progressDialog.setMessage("Signing In")


        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{

            if(it.isSuccessful){

                progressDialog.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
            }
            else {

                progressDialog.dismiss()
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }




            }.addOnFailureListener { exception ->
                progressDialog.dismiss()  // Hata durumunda da progress dialog'u gizle
                when (exception) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, "Auth Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        signInBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        signInBinding.signInTextToSignUp.setOnClickListener {

            startActivity(Intent(this, SignUpActivity::class.java))
        }


        signInBinding.loginButton.setOnClickListener {

            email = signInBinding.loginetmail.text.toString()
            password = signInBinding.loginetpassword.text.toString()

            if (signInBinding.loginetmail.text.isEmpty()) {
                Toast.makeText(this, "Empty cant be empty", Toast.LENGTH_SHORT).show()

            }

            if (signInBinding.loginetpassword.text.isEmpty()) {
                Toast.makeText(this, "Password cant be empty", Toast.LENGTH_SHORT).show()

            }
            if (signInBinding.loginetpassword.text.isNotEmpty() && signInBinding.loginetmail.text.isNotEmpty()) {
                signIn(password, email)


            }


            // Firebase Auth başlat
            auth = FirebaseAuth.getInstance()

            // Progress Dialog oluştur
            progressDialog = AlertDialog.Builder(this)
                .setView(R.layout.custom_progress_dialog)
                .setCancelable(false)
                .create()
        }
        }
    override fun onBackPressed(){ //geri tusuna basıldıgında uygulamanın varsayılan davranışını geri getirir.
        super.onBackPressed()
        progressDialog.dismiss()
        finish()
        }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog.dismiss()
    }
    }




