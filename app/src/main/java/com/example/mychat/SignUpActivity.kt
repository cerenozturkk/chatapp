package com.example.mychat

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.mychat.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("DEPRECATION")
class SignUpActivity : AppCompatActivity() {
    private lateinit var signUpBinding: ActivitySignUpBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var signUpAuth:FirebaseAuth
    private lateinit var name:String
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var signUpPd:ProgressDialog






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        firestore=FirebaseFirestore.getInstance()
        signUpAuth=FirebaseAuth.getInstance()
        signUpPd=ProgressDialog(this)

        signUpBinding.signUpTextToSignIn.setOnClickListener {

            startActivity(Intent(this, SignInActivity::class.java))
        }

        signUpBinding.signUpBtn.setOnClickListener{
            name=signUpBinding.signUpEtName.text.toString()
            email=signUpBinding.signUpEmail.text.toString()
            password=signUpBinding.signUpPassword.text.toString()
            if (signUpBinding.signUpEtName.text.isEmpty()) {
                Toast.makeText(this, "Empty cant be empty", Toast.LENGTH_SHORT).show()

            }

            if (signUpBinding.signUpPassword.text.isEmpty()) {
                Toast.makeText(this, "Password cant be empty", Toast.LENGTH_SHORT).show()

            }
            if (signUpBinding.signUpEmail.text.isEmpty()) {
                Toast.makeText(this, "Email cant be empty", Toast.LENGTH_SHORT).show()

            }
            if (signUpBinding.signUpEtName.text.isNotEmpty() &&
                signUpBinding.signUpEmail.text.isNotEmpty() &&
                signUpBinding.signUpPassword.text.isNotEmpty()) {

                signUpUser(name, password, email)
            } else {
                Toast.makeText(this, "Tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            }



        }

    }

    private fun signUpUser(name: String, password: String, email: String) {

        signUpPd.show()
        signUpPd.setMessage("signing up")

         signUpAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{

             if(it.isSuccessful){
                 val user=signUpAuth.currentUser

                 val hashMap=hashMapOf(
                     "userid" to user!!.uid!!,

                     "username" to name,
                     "useremail" to email,
                     "status" to "default",
                     "imageUrl" to "")

                 firestore.collection("Users").document(user.uid).set(hashMap)
                 signUpPd.dismiss()
                 startActivity(Intent(this,SignInActivity::class.java))


             }

         }

    }
}