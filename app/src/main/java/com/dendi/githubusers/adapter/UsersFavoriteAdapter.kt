package com.dendi.githubusers.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dendi.githubusers.R
import com.dendi.githubusers.databinding.ItemUserListBinding
import com.dendi.githubusers.model.User

class UsersFavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<UsersFavoriteAdapter.UserViewHolder>() {
    var mData = ArrayList<User>()
        set(mData) {
            if (mData.size > 0) {
                this.mData.clear()
            }
            this.mData.addAll(mData)
            notifyDataSetChanged()
        }

    fun addItem(note: User) {
        this.mData.add(note)
        notifyItemInserted(this.mData.size - 1)
    }

    fun updateItem(position: Int, note: User) {
        this.mData[position] = note
        notifyItemChanged(position, note)
    }

    fun removeItem(position: Int) {
        this.mData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.mData.size)
    }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = this.mData.size
}