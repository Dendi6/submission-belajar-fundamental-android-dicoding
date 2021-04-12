package com.dendi.consumerapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dendi.consumerapp.R
import com.dendi.consumerapp.databinding.ItemUserListBinding
import com.dendi.consumerapp.model.User

class UsersFavoriteAdapter(private val activity: Activity) :
    RecyclerView.Adapter<UsersFavoriteAdapter.UserViewHolder>() {
    var dataUsers = ArrayList<User>()
        set(dataUsers) {
            if (dataUsers.size > 0) {
                this.dataUsers.clear()
            }
            this.dataUsers.addAll(dataUsers)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(dataUsers[position])
    }

    override fun getItemCount(): Int = this.dataUsers.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemUserListBinding.bind(itemView)
        fun bind(users: User) {
            with(itemView) {
                binding.txtName.text = users.userName
                Glide.with(itemView.context)
                    .load(users.photo)
                    .into(binding.imgPhoto)
            }
        }
    }
}