package com.example.mychat.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mychat.MyApplication
import com.example.mychat.SharedPrefs
import com.example.mychat.modal.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.installations.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ChatAppViewModel :ViewModel() {

    val name=MutableLiveData<String>()
    val imageUrl=MutableLiveData<String>()
    val message=MutableLiveData<String>()
    private val firestore=FirebaseFirestore.getInstance()


    val usersRepo=UsersRepo()

    init{
        getCurrentUser()


    }


    fun getUsers():LiveData<List<Users>>{

        return usersRepo.getUsers()


    }

    //get current user information

    fun getCurrentUser() = viewModelScope.launch(Dispatchers.IO) {

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            // Kullanıcı giriş yapmamışsa çık
            return@launch
        }

        val userId = currentUser.uid  // Kullanıcının UID'sini al

        firestore.collection("Users").document(userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    // Hata varsa logla
                    return@addSnapshotListener
                }

                if (value != null && value.exists()) {
                    val users = value.toObject(Users::class.java)
                    name.postValue(users?.username ?: "Bilinmeyen Kullanıcı")
                    imageUrl.postValue(users?.imageUrl ?: "")

                    val mySharedPrefs= SharedPrefs(MyApplication.instance.applicationContext)
                    mySharedPrefs.setValue("username",users?.username?:"bilinmeyen kullanıcı")

                }
            }
    }}
