package com.example.mychat.notifications.network

import com.example.mychat.notifications.constans.Companion.BASE_URL

class RetrofitInstance {

    companion object{

        private val retrofit by lazy{

            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create().build())
        }
    }
}