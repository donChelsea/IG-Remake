package com.example.ig_remake.presentation.messages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ig_remake.databinding.FragmentNewMessageBinding
import com.example.ig_remake.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class NewMessageFragment : Fragment() {
    private lateinit var binding: FragmentNewMessageBinding

    private val TAG = "NewMessageFragment"
    private val users  = mutableListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchUsers()

        binding.newMessageBackButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun initalizeRecyclerview(users: List<User>) {
        val adapter = NewMessageAdapter(users)
        binding.newMessageRecyclerview.adapter = adapter
    }

    private fun fetchUsers() {
        val userDb = FirebaseDatabase.getInstance().reference.child("users")
        userDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    Log.d(TAG, it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        users.add(user)
                    }
                }
                initalizeRecyclerview(users)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, error.message)
            }
        })
    }
}