package com.example.mychat.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mychat.Utils
import com.example.mychat.modal.Users
import com.google.firebase.firestore.FirebaseFirestore

class UsersRepo {

    private var firestore = FirebaseFirestore.getInstance()

    fun getUsers(): LiveData<List<Users>> {
        val users = MutableLiveData<List<Users>>()

        firestore.collection("Users").addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                users.value = emptyList() // Hata durumunda boş liste gönder
                return@addSnapshotListener
            }

            val usersList = mutableListOf<Users>()

            snapshot?.documents?.forEach { document ->
                val user = document.toObject(Users::class.java)

                // Null kontrolü yapıldı
                user?.let {
                    // Kullanıcı kendi hesabını görmesin
                    if (it.userid != Utils.getUiLoggedIn()) {
                        usersList.add(it)
                    }
                }
            }

            // Snapshot dinlemesi bittikten sonra LiveData'yı güncelle
            users.value = usersList
        }

        return users
    }
}
