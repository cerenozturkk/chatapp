package com.example.mychat.fragments

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mychat.R
import com.example.mychat.adapter.OnUserClickListener
import com.example.mychat.adapter.UserAdapter
import com.example.mychat.databinding.FragmentHomeBinding
import com.example.mychat.modal.Users
import com.example.mychat.mvvm.ChatAppViewModel
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView


@Suppress("DEPRECATION")
class HomeFragment : Fragment() ,OnUserClickListener{

    lateinit var rvUsers:RecyclerView
    lateinit var userAdapter: UserAdapter
    lateinit var userViewModel:ChatAppViewModel
    lateinit var homebinding:FragmentHomeBinding
    lateinit var fbauth:FirebaseAuth
    lateinit var toolbar:Toolbar
    lateinit var circleImageView:CircleImageView




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
        fbauth=FirebaseAuth.getInstance()
        toolbar=view.findViewById(R.id.toolbarMain)
        circleImageView=toolbar.findViewById(R.id.tlImage)




        userAdapter=UserAdapter()
        rvUsers=view.findViewById(R.id.rvUsers)

        val layoutManagerUsers=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        rvUsers.layoutManager=layoutManagerUsers

        userViewModel.getUsers().observe(viewLifecycleOwner, {

            userAdapter.setUserList(it)
            userAdapter.setOnUserClickListener(this)
            rvUsers.adapter=userAdapter



        })

        homebinding.logOut.setOnClickListener{
            fbauth.signOut()


        }
    }

    override fun onUserSelected(position: Int, users: Users) {

    }


}
