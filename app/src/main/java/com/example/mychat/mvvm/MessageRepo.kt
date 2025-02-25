package com.example.mychat.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mychat.Utils
import com.example.mychat.modal.Messages
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MessageRepo {

    private val firestore = FirebaseFirestore.getInstance()


    fun getMessages(friendid: String): LiveData<List<Messages>> {
        val messages = MutableLiveData<List<Messages>>()

        val uniqueid = listOf(Utils.getUiLoggedIn(), friendid).sorted().joinToString("")

        firestore.collection("Messages").document(uniqueid).collection("chats")
            .orderBy("time", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->

                if (error != null) {
                    return@addSnapshotListener
                }

                val messageList = mutableListOf<Messages>()

                if (!value!!.isEmpty) {
                    value.documents.forEach { document ->
                        val messageModal = document.toObject(Messages::class.java)
                        if (messageModal != null &&
                            ((messageModal.sender == Utils.getUiLoggedIn() && messageModal.receiver == friendid) ||
                                    (messageModal.sender == friendid && messageModal.receiver == Utils.getUiLoggedIn()))
                        ) {
                            messageList.add(messageModal)
                        }
                    }
                    messages.value = messageList
                }
            }
        return messages
    }
}
