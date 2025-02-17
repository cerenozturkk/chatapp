package com.example.mychat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mychat.R
import com.example.mychat.adapter.UserAdapter
import com.example.mychat.databinding.FragmentHomeBinding
import com.example.mychat.mvvm.ChatAppViewModel


class HomeFragment : Fragment() {

    lateinit var rvUsers:RecyclerView
    lateinit var userAdapter: UserAdapter
    lateinit var userViewModel:ChatAppViewModel
    lateinit var homebinding:FragmentHomeBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homebinding=DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)

        return homebinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel=ViewModelProvider(this).get(ChatAppViewModel::class.java)

        userAdapter=UserAdapter()
        rvUsers=view.findViewById(R.id.rvUsers)

        val layoutManagerUsers=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        rvUsers.layoutManager=layoutManagerUsers

        userViewModel.getUsers().observe(viewLifecycleOwner, {

            userAdapter.setUserList(it)
            rvUsers.adapter=userAdapter



        })
    }


    }
