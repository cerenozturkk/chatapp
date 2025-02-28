package com.example.mychat.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mychat.MyApplication
import com.example.mychat.SharedPrefs
import com.example.mychat.modal.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.mychat.Utils
import com.example.mychat.modal.Messages
import com.example.mychat.modal.RecentChats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class ChatAppViewModel :ViewModel() {

    val name = MutableLiveData<String>()
    val imageUrl = MutableLiveData<String>()
    val message = MutableLiveData<String>()
    private val firestore = FirebaseFirestore.getInstance()


    val usersRepo = UsersRepo()
    val messagesRepo=MessageRepo()
    val recentChatRepo=ChatListRepo()


    init {
        getCurrentUser()


    }


    fun getUsers(): LiveData<List<Users>> {

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

                    val mySharedPrefs = SharedPrefs(MyApplication.instance.applicationContext)
                    mySharedPrefs.setValue("username", users?.username ?: "bilinmeyen kullanıcı")

                }
            }
    }

    //SEND MESSAGE
    fun sendMessage(sender: String, receiver: String, friendname: String, friendimage: String) =
        viewModelScope.launch(Dispatchers.IO) {

            val context = MyApplication.instance.applicationContext

            val msg = message.value ?: return@launch // Eğer mesaj null ise fonksiyondan çık

            val loggedInUser = Utils.getUiLoggedIn()
            if (loggedInUser.isNullOrEmpty()) {
                Log.e("ChatAppViewModel", "getUiLoggedIn() null döndü!")
                return@launch
            }

            val hashMap = hashMapOf<String, Any>(
                "sender" to sender,
                "receiver" to receiver,
                "message" to msg,
                "time" to Utils.getTime()
            )

            val uniqueId = listOf(sender, receiver).sorted().joinToString(separator = "")

            val friendnamesplit = friendname.split("\\s".toRegex())[0]
            val mysharedPrefs = SharedPrefs(context)

            mysharedPrefs.setValue("friendid", receiver)
            mysharedPrefs.setValue("chatroomid", uniqueId)
            mysharedPrefs.setValue("friendname", friendnamesplit)
            mysharedPrefs.setValue("friendimage", friendimage)

            // Mesajı Firestore'a gönderme
            firestore.collection("Messages").document(uniqueId)
                .collection("chats").document(Utils.getTime()).set(hashMap)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.e("ChatAppViewModel", "Mesaj gönderme başarısız", task.exception)
                        return@addOnCompleteListener
                    }

                    val hashMapForRecent = hashMapOf<String, Any>(
                        "friendid" to receiver,
                        "time" to Utils.getTime(),
                        "sender" to loggedInUser,
                        "message" to msg,
                        "friendsimage" to friendimage,
                        "name" to friendname,
                        "person" to "you"
                    )


                    firestore.collection("Conversation")
                        .document(uniqueId.toString()) // Bu, sohbet ID'sini içeren ana belge
                        .collection("Chats") // Alt koleksiyon olarak "Chats" kullanılıyor
                        .add(hashMap) // add() kullanarak yeni bir belge oluşturuluyor

                    firestore.collection("Conversation")
                        .document(uniqueId)  // uniqueId burada kullanılıyor
                        .collection("Chats")
                        .document(loggedInUser)  // Belgeyi güncellerken loggedInUser kullanılıyor
                        .update(
                            "message", msg,
                            "time", Utils.getTime(),
                            "person", name.value ?: "Bilinmeyen Kullanıcı"
                        )


                    if(task.isSuccessful){
                        message.value=""



                    }
                }
        }

    fun getMessages(friendid:String):LiveData<List<Messages>>{

        return messagesRepo.getMessages(friendid)

    }
    fun getRecentChats():LiveData<List<RecentChats>>{

        return recentChatRepo.getAllChatList()

    }
}