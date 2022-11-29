package com.example.mungnyang

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mungnyang.databinding.AlluserItemBinding

class UserAdapter(val context: Context, val userList:MutableList<User>):RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = AlluserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val userViewHolder = UserViewHolder(binding)

        return userViewHolder
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val binding = (holder as UserViewHolder).binding
        val user = userList.get(position)

        binding.ivItemUser.setImageResource(R.drawable.user)
        binding.tvItemName.text = user.name
        binding.tvItemEmail.text = user.email

        binding.root.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", user.name)
            intent.putExtra("uId", user.uId)

            context.startActivity(intent)
            //아래 리스트에 History 대화방 남기는 것
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(val binding: AlluserItemBinding):RecyclerView.ViewHolder(binding.root)
}