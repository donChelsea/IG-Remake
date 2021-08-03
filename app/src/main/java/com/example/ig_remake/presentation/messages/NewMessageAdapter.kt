package com.example.ig_remake.presentation.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ig_remake.databinding.ItemNewMessageBinding
import com.example.ig_remake.models.User
import com.squareup.picasso.Picasso

class NewMessageAdapter(private val users: List<User>) :
    RecyclerView.Adapter<NewMessageAdapter.NewMessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewMessageViewHolder {
        return NewMessageViewHolder(
            ItemNewMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewMessageViewHolder, position: Int) {
        holder.onBind(users[position])
    }

    override fun getItemCount() = users.size

    inner class NewMessageViewHolder(val binding: ItemNewMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(user: User) {
            binding.itemNameTv.text = user.username
            Picasso.get().load(user.profileImageUrl).into(binding.itemProfileIv)
        }
    }
}