package com.example.mychat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mychat.R
import com.example.mychat.Utils
import com.example.mychat.adapter.MessageAdapter
import com.example.mychat.databinding.FragmentChatBinding
import com.example.mychat.modal.Messages
import com.example.mychat.mvvm.ChatAppViewModel
import de.hdodenhof.circleimageview.CircleImageView

class ChatFragment : Fragment() {

    private lateinit var args: ChatFragmentArgs
    private lateinit var chatbinding: FragmentChatBinding
    private lateinit var chatAppViewModel: ChatAppViewModel
    private lateinit var chattoolbar: Toolbar
    private lateinit var circleImageView: CircleImageView
    private lateinit var tvusername: TextView
    private lateinit var tvStatus: TextView
    private lateinit var backbtn: ImageView
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        chatbinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        return chatbinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args = ChatFragmentArgs.fromBundle(requireArguments())
        chatAppViewModel = ViewModelProvider(this).get(ChatAppViewModel::class.java)

        chattoolbar = chatbinding.toolBarChat
        circleImageView = view.findViewById(R.id.chatImageViewUser)
        tvStatus = view.findViewById(R.id.chatUserStatus)
        tvusername = view.findViewById(R.id.chatUserName)
        backbtn = chattoolbar.findViewById(R.id.chatBackBtn)

        backbtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_ChatFragment_to_HomeFragment)
        }

        chatbinding.chatBackBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_ChatFragment_to_HomeFragment)
        }

        Glide.with(requireContext()).load(args.users.imageUrl).into(circleImageView)
        tvStatus.text = args.users.status
        tvusername.text = args.users.username
        chatbinding.viewModel = chatAppViewModel
        chatbinding.lifecycleOwner = viewLifecycleOwner

        chatbinding.sendBtn.setOnClickListener {
            chatAppViewModel.sendMessage(
                Utils.getUiLoggedIn(),
                args.users.userid!!,
                args.users.username!!,
                args.users.imageUrl!!
            )
        }

        chatAppViewModel.getMessages(args.users.userid!!).observe(viewLifecycleOwner, Observer { messages ->
            messages?.let {
                initRecyclerView(it)
            }
        })
    }

    private fun initRecyclerView(messages: List<Messages>) {
        messageAdapter = MessageAdapter()
        val layoutManager = LinearLayoutManager(context)
        chatbinding.messagesRecyclerView.layoutManager = layoutManager
        layoutManager.stackFromEnd = true
        messageAdapter.setMessageList(messages)
        messageAdapter.notifyDataSetChanged()
        chatbinding.messagesRecyclerView.adapter = messageAdapter
    }
}
