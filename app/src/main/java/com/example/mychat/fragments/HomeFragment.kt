package com.example.mychat.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mychat.R
import com.example.mychat.adapter.OnRecentChatClicked
import com.example.mychat.adapter.OnUserClickListener
import com.example.mychat.adapter.RecentChatAdapter
import com.example.mychat.adapter.UserAdapter
import com.example.mychat.databinding.FragmentHomeBinding
import com.example.mychat.modal.RecentChats
import com.example.mychat.modal.Users
import com.example.mychat.mvvm.ChatAppViewModel
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView


@Suppress("DEPRECATION")
class HomeFragment : Fragment(), OnUserClickListener, OnRecentChatClicked {

    lateinit var rvUsers: RecyclerView
    lateinit var userAdapter: UserAdapter
    lateinit var userViewModel: ChatAppViewModel
    lateinit var homebinding: FragmentHomeBinding
    lateinit var fbauth: FirebaseAuth
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var circleImageView: CircleImageView
    lateinit var recentchatadapter:  RecentChatAdapter

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

        homebinding.tlImage.setOnClickListener{
            findNavController().navigate(R.id.action_HomeFragment_to_SettingFragment)
        }

        userViewModel.imageUrl.observe(viewLifecycleOwner, { url ->
            Glide.with(requireContext())
                .load(url)
                .into(circleImageView)

        })

        recentchatadapter=RecentChatAdapter()

        userViewModel.getRecentChats().observe(viewLifecycleOwner, Observer{
            Log.d("HomeFragment", "Recent Chats: $it")

            homebinding.rvRecentChats.layoutManager=LinearLayoutManager(activity)

            recentchatadapter.setOnRecentList(it)
            homebinding.rvRecentChats.adapter=recentchatadapter
        })


        recentchatadapter.setOnRecentChatListener(this)





    }

    override fun onUserSelected(position: Int, users: Users) {

        val action=HomeFragmentDirections.actionHomeFragmentToChatFragment(users)
        view?.findNavController()?.navigate(action)

        Log.e("HOMEFRAGMENT","ClickedOn${users.username}")

        //Toast.makeText(requireContext(),"ClickedOn${users.username}",Toast.LENGTH_SHORT).show()


    }

    override fun getOnRecentChatClicked(position: Int, recentChat: RecentChats) {

        val action=HomeFragmentDirections.actionHomeFragmentToChatFromHomeFragment(recentChat)

        view?.findNavController()?.navigate(action)

    }


}

