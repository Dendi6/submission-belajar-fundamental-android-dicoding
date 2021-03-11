package com.dendi.githubusers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class UserAdapter internal constructor(private val context: Context) : BaseAdapter() {
    internal var users = arrayListOf<User>()
    override fun getCount(): Int {
        return users.size
    }

    override fun getItem(i: Int): Any {
        return users[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        var itemView = view
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_user_list, viewGroup, false)
        }
        val viewHolder = ViewHolder(itemView as View)
        val hero = getItem(position) as User
        viewHolder.bind(hero)
        return itemView
    }

    private inner class ViewHolder internal constructor(view: View) {
        private val txtName: TextView = view.findViewById(R.id.txt_name)
        private val txtDescription: TextView = view.findViewById(R.id.txt_description)
        private val imgPhoto: ImageView = view.findViewById(R.id.img_photo)
        internal fun bind(hero: User) {
            txtName.text = hero.name
            txtDescription.text = hero.company
            hero.photo?.let { imgPhoto.setImageResource(it) }
        }
    }
}