@file:Suppress("DEPRECATION")

package com.example.mychat.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.mychat.R
import com.example.mychat.Utils
import com.example.mychat.Utils.Companion.REQUEST_IMAGE_PICK
import com.example.mychat.databinding.FragmentSettingBinding
import com.example.mychat.mvvm.ChatAppViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SettingFragment : Fragment() {


    private lateinit var settingbinding: FragmentSettingBinding

    lateinit var settingviewModel:ChatAppViewModel
    private lateinit var storageRef:StorageReference
    lateinit var storage:FirebaseStorage
    var uri: Uri?=null
    lateinit var bitmap: Bitmap








    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        settingbinding= DataBindingUtil.inflate(inflater,R.layout.fragment_setting,container,false)
        return settingbinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingviewModel=ViewModelProvider(this).get(ChatAppViewModel::class.java)

        settingbinding.viewModel=settingviewModel
        settingbinding.lifecycleOwner=viewLifecycleOwner

        storage=FirebaseStorage.getInstance()
        storageRef=storage.reference

        settingviewModel.imageUrl.observe(viewLifecycleOwner, {

            Glide.with(requireContext()).load(it).placeholder(R.drawable.person).dontAnimate().into(settingbinding.settingUpdateImage)


        })

        settingbinding.settingBackBtn.setOnClickListener{
            view?.findNavController()?.navigate(R.id.action_SettingFragment_to_HomeFragment)

        }

        settingbinding.settingUpdateImage.setOnClickListener{
            val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Choose your profile picture")
            builder.setItems(options) { dialog, item ->
                when {
                    options[item] == "Take Photo" -> {

                        takePhotoWithCamera()


                    }
                    options[item] == "Choose from Gallery" -> {
                        pickImageFromGallery()
                    }
                    options[item] == "Cancel" -> dialog.dismiss()
                }
            }
            builder.show()






        }



    }

    private fun pickImageFromGallery() {

        val pickPictureIntent=
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        if(pickPictureIntent.resolveActivity(requireActivity().packageManager)!=null){
            startActivityForResult(pickPictureIntent, REQUEST_IMAGE_PICK)



        }

    }

    private fun takePhotoWithCamera() {
        val pickPictureIntent=
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

    }




    }
