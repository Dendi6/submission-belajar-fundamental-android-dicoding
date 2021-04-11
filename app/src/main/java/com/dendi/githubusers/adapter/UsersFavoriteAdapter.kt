package com.dendi.githubusers.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dendi.githubusers.R
import com.dendi.githubusers.databinding.ItemUserListBinding
import com.dendi.githubusers.helper.CustomOnItemClickListener
import com.dendi.githubusers.model.User
import com.dendi.githubusers.view.DetailUser

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
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)
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

                itemView.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val user = User(users.id,users.photo,users.userName)
                                val intent = Intent(activity, DetailUser::class.java)
                                intent.putExtra(DetailUser.EXTRA_DATA, user)
                                activity.startActivityForResult(intent, DetailUser.REQUEST_UPDATE)
                            }
                        })
                )
            }
        }
    }
}