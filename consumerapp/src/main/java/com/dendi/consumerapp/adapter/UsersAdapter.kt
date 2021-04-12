package com.dendi.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dendi.consumerapp.R
import com.dendi.consumerapp.databinding.ItemUserListBinding
import com.dendi.consumerapp.model.User

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    private val mData = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserListBinding.bind(itemView)
        fun bind(users: User) {
            with(itemView){
                binding.txtName.text = users.userName
                Glide.with(itemView.context)
                        .load(users.photo)
                        .into(binding.imgPhoto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size
}