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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mychat.R
import com.example.mychat.adapter.OnUserClickListener
import com.example.mychat.adapter.UserAdapter
import com.example.mychat.databinding.FragmentHomeBinding
import com.example.mychat.modal.Users
import com.example.mychat.mvvm.ChatAppViewModel
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView


@Suppress("DEPRECATION")
class HomeFragment : Fragment(), OnUserClickListener {

    lateinit var rvUsers: RecyclerView
    lateinit var userAdapter: UserAdapter
    lateinit var userViewModel: ChatAppViewModel
    lateinit var homebinding: FragmentHomeBinding
    lateinit var fbauth: FirebaseAuth
    lateinit var toolbar: androidx.appcompat.widget.Toolbar  // doğru türde Toolbar kullanıyoruz
    lateinit var circleImageView: CircleImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homebinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return homebinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this).get(ChatAppViewModel::class.java)
        fbauth = FirebaseAuth.getInstance()

        // Toolbar'ı doğru türde alıyoruzz
        toolbar = view.findViewById(R.id.toolbarMain) as androidx.appcompat.widget.Toolbar
        circleImageView = toolbar.findViewById(R.id.tlImage)

        homebinding.lifecycleOwner = viewLifecycleOwner

        userAdapter = UserAdapter()
        rvUsers = view.findViewById(R.id.rvUsers)

        val layoutManagerUsers = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvUsers.layoutManager = layoutManagerUsers

        userViewModel.getUsers().observe(viewLifecycleOwner, {
            userAdapter.setUserList(it)
            userAdapter.setOnUserClickListener(this)
            rvUsers.adapter = userAdapter
        })

        homebinding.logOut.setOnClickListener {
            fbauth.signOut()
            findNavController().navigate(R.id.action_HomeFragment_to_ChatFragment)
        }

        userViewModel.imageUrl.observe(viewLifecycleOwner, { url ->
            Glide.with(requireContext())
                .load(url)
                .into(circleImageView)
        })
    }

    override fun onUserSelected(position: Int, users: Users) {
        // Implement the user selection logic here
    }
}

