package com.dendi.githubusers.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dendi.githubusers.view.fragment.FollowerFollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity)  {
    var username:String = " "

    override fun createFragment(position: Int): Fragment {
        return FollowerFollowingFragment.newInstance(position,username)
    }
    override fun getItemCount(): Int {
        return 2
    }
}