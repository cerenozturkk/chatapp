package com.example.mychat.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mychat.modal.Users

class ChatAppViewModel :ViewModel() {

    val name=MutableLiveData<String>()
    val imageUrl=MutableLiveData<String>()
    val message=MutableLiveData<String>()


    val usersRepo=UsersRepo()


    fun getUsers():LiveData<List<Users>>{

        return usersRepo.getUsers()


    }
}